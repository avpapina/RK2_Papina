package com.hfad.rk2

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hfad.rk2.ui.theme.RK2Theme

class MainActivity : ComponentActivity() {
    private val viewModel: ImageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RK2Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ImageScreen(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun ImageScreen(viewModel: ImageViewModel){
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    when(state){
        is ImageState.Loading -> {
            LoadingScreen()
        }

        is ImageState.Error -> {
            val  error = (state as ImageState.Error).message
            ErrorScreen(error = error, onRetry =  { viewModel.loadImages()})
        }

        is ImageState.Success -> {
            val images = (state as ImageState.Success).images

            LazyColumn (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp)
            ){
                item {
                    Text(
                        text = stringResource(R.string.gallery_title),
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }

                items (images) { image ->
                    val imageNumber = images.indexOf(image) + 1
                    ImageCard(
                        imageUrl = image.images.fixed_width.url,
                        imageNumber = imageNumber,
                        onClick = {
                            Toast.makeText(
                                context,
                                "Изображение №${imageNumber}",
                                //stringResource(R.string.image_number, image.imageNumber),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                }
            }

        }
    }
}

@Composable
fun  LoadingScreen(){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorScreen(error: String, onRetry: () -> Unit){
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Text(
            text = stringResource(R.string.error),
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = error,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        Button(onClick = onRetry) {
            Text(stringResource(R.string.retry))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GalleryScreenPreview() {
    MaterialTheme {
        ImageScreen(viewModel = ImageViewModel())
    }
}
