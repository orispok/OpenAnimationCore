package com.osg.core.components.report

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.osg.openanimation.core.ui.components.report.ReportUiState
import com.osg.openanimation.core.ui.components.report.ReportViewModel
import com.osg.openanimation.core.ui.components.report.displayName
import kotlinx.coroutines.delay
import com.osg.openanimation.core.data.animation.AnimationMetadata
import com.osg.openanimation.core.data.report.ReportReasonOptions
import com.osg.openanimation.core.data.report.ReportSubmission


@Composable
fun AnimationReport(
    animationMetadata: AnimationMetadata,
    onDismissRequest: () -> Unit,
) {
    Dialog(onDismissRequest = onDismissRequest) {
        val reportViewModel = viewModel { ReportViewModel() }
        val uiState by reportViewModel.uiState.collectAsState()
        when(val reportState = uiState){
            is ReportUiState.Initial -> {
                AnimationReportContent(
                    animationMetadata = animationMetadata,
                    onReportClick = reportViewModel::submitReport,
                    onDismiss = onDismissRequest
                )
            }
            is ReportUiState.SubmitProcess -> {
                // Show loading indicator
                AnimationReportProcess(
                    submitProcess = reportState,
                    onDismiss = onDismissRequest
                )
            }
        }

    }
}

@Composable
fun AnimationReportProcess(
    submitProcess: ReportUiState.SubmitProcess,
    onDismiss: () -> Unit,
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(16.dp),
    ) {
        Box(modifier = Modifier){
            when(submitProcess){
                ReportUiState.SubmitProcess.Sending -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.Center),
                        color = MaterialTheme.colorScheme.primary,
                        strokeWidth = 4.dp
                    )
                }
                ReportUiState.SubmitProcess.Sent -> {
                    Text(
                        text = "Report submitted successfully. Thank you for your feedback.",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .align(Alignment.Center),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                    )
                    LaunchedEffect(Unit) {
                        delay(1500)
                        onDismiss()
                    }
                }
            }
        }
    }

}

@Composable
fun AnimationReportContent(
    animationMetadata: AnimationMetadata,
    onReportClick: (ReportSubmission) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedReason by remember { mutableStateOf(ReportReasonOptions.INAPPROPRIATE) }
    var additionalInfo by remember { mutableStateOf("") }
    
    Card(
        modifier = Modifier.padding(16.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // Title
            Text(
                text = "Report Animation",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Animation Information
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surfaceVariant,
                tonalElevation = 1.dp,
                shape = MaterialTheme.shapes.small
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "Reporting:",
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        text = animationMetadata.name,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    if (animationMetadata.author != null) {
                        Text(
                            text = "By: ${animationMetadata.author}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Report Reason
            Text(
                text = "Reason for report:",
                style = MaterialTheme.typography.titleMedium
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Radio buttons for report reasons
            ReportReasonOptions.entries.forEach { option ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .selectable(
                            selected = (option == selectedReason),
                            onClick = { selectedReason = option },
                            role = Role.RadioButton
                        )
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (option == selectedReason),
                        onClick = null // null because the parent is selectable
                    )
                    Text(
                        text = option.displayName,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Additional information
            OutlinedTextField(
                value = additionalInfo,
                onValueChange = { additionalInfo = it },
                label = { Text("Additional information") },
                placeholder = { Text("Please provide more details about the issue...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                minLines = 3,
                maxLines = 5
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = onDismiss
                ) {
                    Text("Cancel")
                }
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Button(
                    onClick = {
                        onReportClick(
                            ReportSubmission(
                                reason = selectedReason,
                                moreInfo = additionalInfo,
                                animationHash = animationMetadata.hash
                            )
                        )
                    },
                    enabled = additionalInfo.isNotBlank()
                ) {
                    Text("Submit Report")
                }
            }
        }
    }
}