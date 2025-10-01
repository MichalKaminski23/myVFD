package com.vfd.client.ui.components.layout

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vfd.client.ui.components.buttons.AppLoadMoreButton
import com.vfd.client.ui.components.elements.AppSearchBar
import com.vfd.client.ui.components.texts.AppErrorText
import com.vfd.client.ui.components.texts.AppText

@Composable
fun <T> AppListScreen(
    data: List<T>,
    isLoading: Boolean,
    hasPermission: Boolean,
    noPermissionText: String,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    searchPlaceholder: String,
    filter: (T, String) -> Boolean,
    emptyText: String,
    emptyFilteredText: String,
    hasMore: Boolean,
    onLoadMore: () -> Unit,
    errorMessage: String?,
    itemKey: ((T) -> Any)? = null,
    itemContent: @Composable (item: T) -> Unit,
) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        item {
            AppSearchBar(
                query = searchQuery,
                onQueryChange = onSearchChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = searchPlaceholder,
                enabled = !isLoading,
                loading = isLoading,
            )
            Spacer(Modifier.height(12.dp))
        }

        if (!hasPermission) {
            item {
                AppText(
                    noPermissionText,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
        } else {
            val filtered =
                if (searchQuery.isBlank()) data else data.filter { filter(it, searchQuery) }

            if (filtered.isEmpty()) {
                item {
                    AppText(
                        if (searchQuery.isBlank()) emptyText else emptyFilteredText,
                        style = MaterialTheme.typography.headlineLarge
                    )
                }
            } else {
                if (itemKey != null) {
                    items(filtered, key = itemKey) { item ->
                        itemContent(item)
                        Spacer(Modifier.height(12.dp))
                    }
                } else {
                    items(filtered) { item ->
                        itemContent(item)
                        Spacer(Modifier.height(12.dp))
                    }
                }
            }
        }

        item {
            Spacer(Modifier.height(12.dp))
            AppLoadMoreButton(
                hasMore = hasMore,
                isLoading = isLoading,
                onLoadMore = {
                    if (hasMore && !isLoading) onLoadMore()
                }
            )
        }

        if (errorMessage != null) {
            item {
                AppErrorText(errorMessage)
            }
        }
    }
}
