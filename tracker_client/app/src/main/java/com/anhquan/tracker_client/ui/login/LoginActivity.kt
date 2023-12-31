package com.anhquan.tracker_client.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.anhquan.tracker_client.R
import com.anhquan.tracker_client.common.composables.setView
import com.anhquan.tracker_client.ui.done.DoneActivity
import com.anhquan.tracker_client.ui.signUp.SignUpActivity

class LoginActivity : ComponentActivity() {
    private val viewModel by viewModels<LoginViewModel>()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setView {
            val login by viewModel.loggedIn.collectAsState()

            var email by remember {
                mutableStateOf("")
            }
            var password by remember {
                mutableStateOf("")
            }
            var passwordVisibility: Boolean by remember { mutableStateOf(false) }

            when (login) {
                LoginViewModel.LoginState.LOGGED_IN -> {
                    LaunchedEffect(login) {
                        startActivity(Intent(this@LoginActivity, DoneActivity::class.java))
                        finish()
                    }
                }

                LoginViewModel.LoginState.INVALID_EMAIL -> {
                    LaunchedEffect(email) {
                        Toast.makeText(
                            this@LoginActivity,
                            getString(R.string.invalid_email),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                LoginViewModel.LoginState.INVALID_PASSWORD -> {
                    LaunchedEffect(password) {
                        Toast.makeText(
                            this@LoginActivity,
                            getString(R.string.invalid_password),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                LoginViewModel.LoginState.EXCEPTION -> {
                    LaunchedEffect(login) {
                        Toast.makeText(
                            this@LoginActivity,
                            getString(R.string.incorrect_credential),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                else -> {}
            }

            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(stringResource(id = R.string.app_name))
                        },
                    )
                },
                contentWindowInsets = ScaffoldDefaults.contentWindowInsets.add(
                    WindowInsets(
                        left = 16.dp, right = 16.dp
                    )
                ).add(WindowInsets.ime),
            ) { p ->
                Column(
                    modifier = Modifier
                        .padding(p)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        getString(R.string.login),
                        fontSize = 24.sp,
                        modifier = Modifier.padding(bottom = 32.dp)
                    )
                    TextField(
                        shape = RoundedCornerShape(12),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            errorIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                        ),
                        label = {
                                Text("Email")
                        },
                        placeholder = {
                            Text("Nhập email")
                        },
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
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    )
                    TextField(value = password,
                        shape = RoundedCornerShape(12),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            errorIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                        ),
                        label = {
                            Text("Mật khẩu")
                        },
                        placeholder = {
                            Text("Nhập mật khẩu")
                        },
                        onValueChange = {
                            password = it
                        },
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.None,
                            autoCorrect = false,
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done,
                        ),
                        keyboardActions = KeyboardActions(onDone = {
                            viewModel.login(email, password)
                        }),
                        singleLine = true,
                        visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = {
                                passwordVisibility = !passwordVisibility
                            }) {
                                if (!passwordVisibility) Icon(
                                    painterResource(id = R.drawable.visibility),
                                    null
                                )
                                else Icon(painterResource(id = R.drawable.visibility_off), null)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 32.dp)
                    )
                    Button(onClick = {
                        viewModel.login(email, password)
                    }, modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .padding(bottom = 8.dp)) {
                        Text(getString(R.string.login))
                    }
                    Button(onClick = {
                        startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
                    }, modifier = Modifier.fillMaxWidth(0.7f)) {
                        Text(getString(R.string.sign_up))
                    }
                }
            }
        }
    }
}