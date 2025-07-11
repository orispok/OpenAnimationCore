package com.osg.openanimation.core.ui.components.signin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.osg.openanimation.core.ui.components.lottie.AnimationDataState
import com.osg.openanimation.core.ui.components.lottie.LottieAnimationView
import com.osg.openanimation.core.ui.di.SignInProviderFactory
import com.osg.openanimation.core.ui.di.UserRepository
import org.koin.compose.koinInject

@Composable
fun SignInReasoningDialog(
    signInService: SignInProviderFactory = koinInject(),
    userRepository: UserRepository = koinInject(),
    onDismissRequest: () -> Unit,
){
    Dialog(
        onDismissRequest = onDismissRequest,
        content = {
            Card(
                modifier = Modifier
                    .height(375.dp)
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                SignInReasoningContent(
                    signInProviders = signInService.buildSignInProviders(),
                    modifier = Modifier,
                    onRegistered = {
                        userRepository.onRegistered(it)
                        onDismissRequest()
                    }
                )
            }
        }
    )
}

@Composable
fun SignInReasoningDialogView(
    modifier: Modifier = Modifier,
){
    val signInService: SignInProviderFactory = koinInject()
    val userRepository: UserRepository = koinInject()
    SignInReasoningContent(
        signInProviders = signInService.buildSignInProviders(),
        modifier = modifier,
        onRegistered = userRepository::onRegistered
    )
}


@Composable
fun SignInReasoningContent(
    signInProviders: List<SignInProvider>,
    onRegistered: (Result<SignInResult>) -> Unit,
    modifier: Modifier = Modifier,
){
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val animationsState = AnimationDataState.fromResource(
            "files/sign_in_first_animation.json"
        )
        LottieAnimationView(
            animationData = animationsState,
            modifier = Modifier
                .height(160.dp),
            contentScale = ContentScale.Fit,
        )
        Text(
            text = "Sign in and enjoy the full experience",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.titleMedium,
        )

        SignInControllers(
            signInProviders = signInProviders,
            onComplete = onRegistered
        )
    }
}