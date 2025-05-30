package com.osg.appUiLayer.components.signin

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.osg.appUiLayer.util.resource.string
import com.osg.appUiLayer.components.shimmer.shimmerLoadingAnimation
import com.osg.multibase.signin.domain.icons.googleIcon
import openanimation.appuilayer.generated.resources.Res
import openanimation.appuilayer.generated.resources.sign_button_google
import org.jetbrains.compose.resources.StringResource

@Immutable
sealed class BrandedButtonType(
    val imageVector: ImageVector,
    val iconHeight: Dp,
    val backgroundColor: Color,
    val textColor: Color,
    val stringRes: StringResource
) {
    data object GoogleButton : BrandedButtonType(
        imageVector = googleIcon,
        iconHeight = 25.dp,
        backgroundColor = Color.White,
        textColor = Color.Black,
        stringRes = Res.string.sign_button_google
    )
}

val SignInIdentifier.brandedButtonType: BrandedButtonType
    get() = when (this) {
        SignInIdentifier.Google -> BrandedButtonType.GoogleButton
    }

@Composable
fun BrandedButton(
    modifier: Modifier = Modifier,
    brandedButtonType: BrandedButtonType,
    onClick: () -> Unit = {},
    textStyle: TextStyle = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold),
) {
    var isClicked by remember { mutableStateOf(false) }
    ExtendedFloatingActionButton(
        modifier = modifier.aspectRatio(6f).shimmerLoadingAnimation(isClicked, cornerSize = 10.dp),
        shape = RoundedCornerShape(10.dp),
        containerColor = brandedButtonType.backgroundColor,
        onClick = {
            isClicked = true
            onClick()
        } ,
        icon = {
            Image(
                modifier = Modifier.height(brandedButtonType.iconHeight),
                imageVector = brandedButtonType.imageVector,
                contentDescription = null
            )
        },
        text = {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            )
            {
                Text(
                    text = brandedButtonType.stringRes.string,
                    maxLines = 1,
                    style = textStyle.copy(color = brandedButtonType.textColor)
                )
            }

        },
    )
}

