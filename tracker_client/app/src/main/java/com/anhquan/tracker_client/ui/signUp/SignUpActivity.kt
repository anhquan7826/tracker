package com.anhquan.tracker_client.ui.signUp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.anhquan.tracker_client.R
import com.anhquan.tracker_client.common.composables.setView
import com.anhquan.tracker_client.ui.done.DoneActivity
import com.anhquan.tracker_client.utils.debugLog

class SignUpActivity : ComponentActivity() {
    private val viewModel by viewModels<SignUpViewModel>()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setView {
            var passwordVisibility by remember {
                mutableStateOf(false)
            }
            var email by remember {
                mutableStateOf("")
            }
            var password by remember {
                mutableStateOf("")
            }
            var rePassword by remember {
                mutableStateOf("")
            }

            val signUpState by viewModel.signUp.collectAsState()
            debugLog(signUpState)
            when (signUpState) {
                SignUpViewModel.SignInState.SIGNED_UP -> {
                    LaunchedEffect(true) {
                        val intent = Intent(
                            this@SignUpActivity,
                            DoneActivity::class.java
                        ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                    }
                }

                SignUpViewModel.SignInState.INVALID_EMAIL -> {
                    LaunchedEffect(email) {
                        Toast.makeText(
                            this@SignUpActivity,
                            getString(R.string.invalid_email), Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }

                SignUpViewModel.SignInState.INVALID_PASSWORD -> {
                    LaunchedEffect(password) {
                        Toast.makeText(
                            this@SignUpActivity,
                            getString(R.string.invalid_password), Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }

                SignUpViewModel.SignInState.PASSWORD_NOT_MATCH -> {
                    LaunchedEffect(rePassword) {
                        Toast.makeText(
                            this@SignUpActivity,
                            getString(R.string.password_not_match), Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }

                SignUpViewModel.SignInState.EXCEPTION -> {
                    LaunchedEffect(signUpState) {
                        Toast.makeText(
                            this@SignUpActivity,
                            getString(R.string.email_exist), Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }

                else -> {}
            }

            Scaffold(
                topBar = {
                    TopAppBar(title = {
                        Text(text = stringResource(id = R.string.sign_up))
                    }, navigationIcon = {
                        IconButton(onClick = { finish() }) {
                            Icon(
                                painter = painterResource(id = R.drawable.arrow_back),
                                contentDescription = null
                            )
                        }
                    })
                }
            )
            { p ->
                Column(
                    modifier = Modifier.padding(p)
                ) {
                    TextField(
                        value = email,
                        onValueChange = {
                            email = it.trim()
                        },
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.None,
                            autoCorrect = false,
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next,
                        ),
                        singleLine = true
                    )
                    TextField(
                        value = password,
                        onValueChange = {
                            password = it
                        },
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.None,
                            autoCorrect = false,
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Next,
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                viewModel.signUp(email, password, rePassword)
                            }
                        ),
                        singleLine = true,
                        visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = {
                                passwordVisibility = !passwordVisibility
                            }) {
                                if (!passwordVisibility)
                                    Icon(painterResource(id = R.drawable.visibility), null)
                                else
                                    Icon(painterResource(id = R.drawable.visibility_off), null)
                            }
                        }
                    )
                    TextField(
                        value = rePassword,
                        onValueChange = {
                            rePassword = it
                        },
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.None,
                            autoCorrect = false,
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done,
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                viewModel.signUp(email, password, rePassword)
                            }
                        ),
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                    )
                    FilledTonalButton(onClick = {
                        viewModel.signUp(email, password, rePassword)
                    }) {
                        Text(getString(R.string.sign_up))
                    }
                }
            }
        }
    }
}