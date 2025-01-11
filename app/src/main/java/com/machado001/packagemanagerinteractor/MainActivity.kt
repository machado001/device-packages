package com.machado001.packagemanagerinteractor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.rememberAsyncImagePainter
import com.machado001.packagemanagerinteractor.data.AndroidPackageRepository
import com.machado001.packagemanagerinteractor.presentation.HomeViewModel
import com.machado001.packagemanagerinteractor.ui.theme.PackageManagerInteractionTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PackageManagerInteractionTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { TopAppBar() }) { innerPadding ->
                    MainScreenRoot(
                        modifier = Modifier
                            .padding(innerPadding)
                            .height(IntrinsicSize.Min)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(modifier: Modifier = Modifier) {
    androidx.compose.material3.TopAppBar(
        title = { Text("Package Manager") },
        modifier = modifier,
        windowInsets = WindowInsets.systemBars
    )
}

@Composable
fun MainScreenRoot(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val viewModel = HomeViewModel(AndroidPackageRepository(context.packageManager))

    MainScreen(modifier, viewModel = viewModel,)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(),
) {
    val packagesFilteredFromSearch by viewModel.searchResults.collectAsStateWithLifecycle()
    val keyboardController = LocalSoftwareKeyboardController.current

    SearchBar(
        query = viewModel.searchQuery,
        onQueryChange = viewModel::onSearch,
        onSearch = { keyboardController?.hide() },
        placeholder = {
            Text(text = stringResource(R.string.search_package_name))
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = null
            )
        },
        trailingIcon = {
            if (viewModel.searchQuery.isNotEmpty()) {
                IconButton(onClick = { viewModel.onSearch("") }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        tint = MaterialTheme.colorScheme.onSurface,
                        contentDescription = stringResource(R.string.clear_search)
                    )
                }
            }
        },
        active = true,
        onActiveChange = {},
        tonalElevation = 4.dp
    ) {

        if (packagesFilteredFromSearch.isEmpty()) {
            EmptyPackageListAfterSearchColumn()
        } else {
            LazyColumn {
                items(
                    count = packagesFilteredFromSearch.size,
                    key = { index -> packagesFilteredFromSearch[index].name }) { index ->
                    packagesFilteredFromSearch[index].apply {
                        PackageInfoItem(packageName = name, versionName = version)
                    }

                    Spacer(modifier = Modifier.padding(bottom = 4.dp))
                }
            }
        }
    }
}

@Composable
fun EmptyPackageListAfterSearchColumn(
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = stringResource(R.string.no_packages_found)
        )
        Text(
            text = stringResource(R.string.no_packages_found),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun PackageInfoItem(modifier: Modifier = Modifier, packageName: String, versionName: String?) {
    val context = LocalContext.current
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = modifier
                .height(IntrinsicSize.Max)
        ) {
            Image(
                rememberAsyncImagePainter(
                    context.packageManager.getApplicationIcon(
                        packageName
                    )
                ),
                modifier = modifier.size(60.dp),
                contentScale = ContentScale.FillHeight,
                contentDescription = packageName,
            )
            Spacer(modifier = Modifier.padding(end = 8.dp))
            Column {
                Text(text = packageName)
                Text(text = "version: ${versionName ?: "N/A"}")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PackageManagerInteractionTheme {
        MainScreen()
    }
}