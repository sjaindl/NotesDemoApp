package com.sjaindl.notesdemoapp.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SignupOrLoginChooser(
    onSignUp: () -> Unit,
    onLogin: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        OutlinedButton(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(),
            onClick = onSignUp,
        ) {
            Text("Sign Up")
        }

        OutlinedButton(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(),
            onClick = onLogin,
        ) {
            Text("Log In")
        }
    }
}
