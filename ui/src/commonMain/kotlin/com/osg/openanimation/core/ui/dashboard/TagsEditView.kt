package com.osg.openanimation.core.ui.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TagsEditView(
    modifier: Modifier = Modifier,
    tags: Set<String>,
    allTags: Set<String> = emptySet(),
    onTagsChange: (Set<String>) -> Unit
) {
    var newTag by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    Column(
        modifier = modifier
    ) {
        OutlinedTextField(
            value = newTag,
            onValueChange = {
                newTag = it
                expanded = it.isNotBlank()
            },
            label = {
                Text("Add Tags")
            },
            trailingIcon = {

            },

            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (newTag.isNotBlank()) {
                        onTagsChange(tags + newTag)
                        newTag = ""
                        expanded = false
                    }
                }
            ),
            modifier = Modifier.padding(2.dp)
        )

        if (expanded) {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                if (newTag.isNotBlank() && newTag !in tags) {
                    DropdownMenuItem(
                        text = { Text("Add \"$newTag\"") },
                        onClick = {
                            onTagsChange(tags + newTag)
                            newTag = ""
                            expanded = false
                        }
                    )
                }
                val suggestions = allTags.filter {
                    it.contains(
                        newTag,
                        ignoreCase = true
                    ) && it !in tags && it != newTag
                }
                suggestions.take(5).forEach { suggestion ->
                    DropdownMenuItem(
                        text = { Text(suggestion) },
                        onClick = {
                            onTagsChange(tags + suggestion)
                            newTag = ""
                            expanded = false
                        }
                    )
                }
            }
        }


        FlowRow(modifier = Modifier) {
            tags.forEach { tag ->
                InputChip(
                    modifier = Modifier.padding(2.dp),
                    selected = false,
                    onClick = { onTagsChange(tags - tag) },
                    label = { Text(tag) },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Remove tag",
                            modifier = Modifier.padding(2.dp)
                        )
                    }
                )
            }
        }
    }
}
