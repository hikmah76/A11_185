package com.example.perpustakaan.ui.customwidget

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(
    title: String,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    navigateUp: () -> Unit = {},
    onRefresh: () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        title = { Text(title) },
        actions = {
            Icon(imageVector = Icons.Default.Refresh, contentDescription = "", modifier = Modifier.clickable {
                onRefresh()
            })
        },
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
                }
            }
        }
    )
}