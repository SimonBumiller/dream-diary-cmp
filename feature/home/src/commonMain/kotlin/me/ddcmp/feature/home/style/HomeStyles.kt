package me.ddcmp.feature.home.style

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
internal object HomeStyles {

    val dreamItemTitle: TextStyle
        @Composable get() = MaterialTheme.typography.titleMediumEmphasized
            .copy(fontWeight = FontWeight.Bold)

    val dreamItemTitleAlternative: TextStyle
        @Composable get() = MaterialTheme.typography.titleSmallEmphasized
            .copy(fontWeight = FontWeight.Bold)

}