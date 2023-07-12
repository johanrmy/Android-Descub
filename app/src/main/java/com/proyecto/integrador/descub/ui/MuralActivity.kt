package com.proyecto.integrador.descub.ui

import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.proyecto.integrador.descub.R
import com.proyecto.integrador.descub.data.model.Mural
import com.proyecto.integrador.descub.data.service.MuralApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MuralActivity : AppCompatActivity() {
    private lateinit var nombreTextView: TextView
    private lateinit var descripcionTextView: TextView
    private lateinit var direccionTextView: TextView
    private lateinit var coordenadasTextView: TextView
    private lateinit var imagenImageView: ImageView
    private lateinit var creacionTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mural)

        nombreTextView = findViewById(R.id.nombre_text_view)

        descripcionTextView = findViewById(R.id.descripcion_text_view)
        imagenImageView = findViewById(R.id.image_view1)
        direccionTextView = findViewById(R.id.direccion_text_view)
        coordenadasTextView = findViewById(R.id.coordenadas_text_view)
        creacionTextView = findViewById(R.id.creacion_text_view)

        val qrCodeText = intent.getStringExtra("qrCodeText")
        if (qrCodeText != null) {
            fetchDataFromApi(qrCodeText)
        }
    }

    private fun fetchDataFromApi(apiUrl: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://r93clvyv9i.execute-api.us-east-2.amazonaws.com/descubcom/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(MuralApiService::class.java)

        val id = obtenerIdDesdeUrl(apiUrl)

        val call = apiService.getMural(id)
        call.enqueue(object : Callback<Mural> {
            override fun onResponse(call: Call<Mural>, response: Response<Mural>) {
                if (response.isSuccessful) {
                    val mural = response.body()
                    if (mural != null) {
                        val fecha = mural.fecha_creacion.toString()
                        val fecha_transfromada = fecha.substring(0,10)
                        nombreTextView.text = mural.nombre
                        descripcionTextView.text = mural.descripcion
                        direccionTextView.text = mural.direccion
                        coordenadasTextView.text = "("+mural.altitud.toString() + " , " +mural.latitud.toString()+")"
                        creacionTextView.text = fecha_transfromada
                        loadImageFromUrl(mural.imagen1, imagenImageView)
                    }
                }
            }

            override fun onFailure(call: Call<Mural>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    private fun obtenerIdDesdeUrl(apiUrl: String): Int {
        val uri = Uri.parse(apiUrl)
        return uri.getQueryParameter("id")?.toIntOrNull() ?: 0
    }

    private fun loadImageFromUrl(imageUrl: String, imageView: ImageView) {
        Glide.with(this)
            .load(imageUrl)
            .apply(RequestOptions().centerCrop())
            .into(imageView)
    }
}
