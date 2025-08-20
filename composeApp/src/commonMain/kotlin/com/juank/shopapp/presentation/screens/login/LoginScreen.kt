package com.juank.shopapp.presentation.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import com.juank.shopapp.presentation.theme.ColorApp.blueButton
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import shopapp.composeapp.generated.resources.Res
import shopapp.composeapp.generated.resources.email
import shopapp.composeapp.generated.resources.email_error
import shopapp.composeapp.generated.resources.ic_eye_dont_show
import shopapp.composeapp.generated.resources.ic_eye_show
import shopapp.composeapp.generated.resources.password
import shopapp.composeapp.generated.resources.password_error
import shopapp.composeapp.generated.resources.sing_in

class LoginScreen : Screen {
    @Composable
    override fun Content() {
        val loginViewModel = koinInject<LoginViewModel>()
        val uiState = loginViewModel.uiState.collectAsState().value

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) { paddingValues ->
            ContentLogin(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                loginViewModel = loginViewModel,
                uiState = uiState
            )
        }
    }

    @Composable
    fun ContentLogin(modifier: Modifier, loginViewModel: LoginViewModel, uiState: LoginScreenUiState) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextTitle()
            TextLoginFields(uiState, loginViewModel)
            Spacer(modifier = Modifier.weight(0.7f))
            ButtonLogin(loginViewModel)
        }
    }

    @Composable
    fun TextTitle() {
        Text(
            text = stringResource(Res.string.sing_in),
            style = TextStyle(fontWeight = FontWeight.Bold),
            fontSize = 22.sp,
            modifier = Modifier.padding(vertical = 18.dp)
        )
    }

    @Composable
    fun TextLoginFields(uiState: LoginScreenUiState, loginViewModel: LoginViewModel) {

        Column {
            OutlinedTextField(
                value = uiState.email,
                onValueChange = { loginViewModel.setEmail(it) },
                label = {
                    Text(text = stringResource(Res.string.email))
                },
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp, horizontal = 24.dp),
                isError = uiState.emailError,
                supportingText = { if (uiState.emailError) Text(stringResource(Res.string.email_error)) }
            )
            OutlinedTextField(
                value = uiState.password,
                onValueChange = { loginViewModel.setPassword(it) },
                label = {
                    Text(text = stringResource(Res.string.password))
                },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                visualTransformation = if (!uiState.visiblePassword) PasswordVisualTransformation() else VisualTransformation.None,
                trailingIcon = {
                    if (uiState.visiblePassword) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_eye_show),
                            contentDescription = "Show password",
                            modifier = Modifier
                                .size(30.dp)
                                .clickable { loginViewModel.setVisiblePassword(!uiState.visiblePassword) }
                        )
                    } else {
                        Icon(
                            painter = painterResource(Res.drawable.ic_eye_dont_show),
                            contentDescription = "Don't show password",
                            modifier = Modifier
                                .size(30.dp)
                                .clickable { loginViewModel.setVisiblePassword(!uiState.visiblePassword) }
                        )
                    }
                },
                isError = uiState.passwordError,
                supportingText = { if (uiState.passwordError) Text(stringResource(Res.string.password_error)) }
            )
        }
    }

    @Composable
    fun ButtonLogin(loginViewModel: LoginViewModel) {

        Button(
            onClick = { loginViewModel.signIn() },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonColors(
                containerColor = blueButton,
                contentColor = Color.White,
                disabledContentColor = Color.Gray,
                disabledContainerColor = Color.DarkGray)
        ) {
            Text(
                text = stringResource(Res.string.sing_in),
                color = Color.White,
                style = TextStyle(fontWeight = FontWeight.Bold),
                fontSize = 16.sp
            )
        }
    }
}

