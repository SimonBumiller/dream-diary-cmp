package me.bumiller.ddcmp.ui.common.biometric

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import me.bumiller.ddcmp.domain.model.PlatformFeatures

/**
 * Route used for the biometric-check composable.
 */
@PublishedApi
@Serializable
internal data class BiometricCheck(

    /**
     * Fully qualified name of the route-clazz the biometric check is for.
     */
    val forRoute: String
) {

    /**
     * Gets the route name for this biometric route.
     */
    val routeName = "biometric_check/$forRoute"

}

@PublishedApi
@Serializable
internal data class SecureNavigationState(

    /**
     * Fully qualified name of the route-class stored in [serializedRoute].
     */
    val routeClass: String,

    /**
     * The secured route, serialized in json-format.
     */
    val serializedRoute: String
)

@PublishedApi
internal const val SecureNavigationStateKey = "secure_navigation_state"

/**
 * Enables the secure navigation on this nav-graph.
 *
 * @param controller A nav-controller for the respective nav-graph
 */
@PublishedApi
@OptIn(InternalSerializationApi::class)
internal inline fun <reified T : Any> NavGraphBuilder.setRouteSecurable(controller: NavController) {
    val checkRoute = BiometricCheck(T::class.qualifiedName!!)

    // We use dialog so it is displayed over the currently visible content
    dialog(checkRoute.routeName) {
        val state = rememberBiometricState()
        LaunchedEffect(Unit) {
            state.requestAuth()
        }

        BiometricContainer(
            state = state
        ) { result ->
            if(result !is BiometricResult.AuthenticationSuccess) return@BiometricContainer

            val securedState = controller.currentBackStackEntry
                ?.savedStateHandle
                ?.get<String>(SecureNavigationStateKey)
                ?.let { Json.decodeFromString<SecureNavigationState>(it) }
                ?: throw IllegalStateException("Could not find a SecureNavigationState attached to the backStackEntry.")

            val routeSerializer = T::class.serializer()

            val route = Json.decodeFromString(routeSerializer, securedState.serializedRoute)

            controller.navigate(route) {
                popUpTo(checkRoute.routeName) { inclusive = true }
            }
        }
    }
}

/**
 * Sets up a composable and also configures that composable to be securable.
 */
inline fun <reified T : Any> NavGraphBuilder.securableComposable(
    navController: NavController,
    noinline content: @Composable (AnimatedContentScope.(NavBackStackEntry) -> Unit)
) {
    setRouteSecurable<T>(navController)
    composable<T>(content = content)
}

/**
 * Perform a navigation to the route [route], but secure the navigation with biometric authentication.
 *
 * The biometric authentication is only required of [PlatformFeatures.supportsBiometricAuth] is true.
 * Otherwise, this function is equivalent to [NavController.navigate].
 *
 * @param secured Whether the navigation should be performed secured. If false, equivalent to [NavController.navigate].
 */
inline fun <reified T : Any> NavController.navigateSecured(
    route: T,
    secured: Boolean = true,
    noinline builder: (NavOptionsBuilder.() -> Unit)? = null
) {
    if (!PlatformFeatures.supportsBiometricAuth || !secured) {
        navigate(route, builder ?: {})
        return
    }

    try {
        val biometricRoute = BiometricCheck(T::class.qualifiedName!!).routeName
        navigate(biometricRoute)
    } catch (e: Exception) {
        throw IllegalArgumentException("No secured nav-destination was declared for route $route. Ensure it was declared using securableComposable.", e)
    }

    val serializedRoute = Json.encodeToString(route)
    val securedState = SecureNavigationState(route::class.qualifiedName!!, serializedRoute)
    val serializedSecuredState = Json.encodeToString(securedState)

    currentBackStackEntry
        ?.savedStateHandle
        ?.set(SecureNavigationStateKey, serializedSecuredState)
}