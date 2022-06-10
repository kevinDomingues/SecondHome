package ipvc.estg.secondhome

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import ipvc.estg.secondhome.api.EndPoints
import ipvc.estg.secondhome.api.ServiceBuilder
import ipvc.estg.secondhome.models.DefaultResponse
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class InsertAds : Fragment(R.layout.fragment_insert_ads) {

    val REQUEST_CODE = 200

    private var files: ArrayList<Uri> = ArrayList()

    lateinit var file: Uri

    lateinit var sharedPreferences: SharedPreferences

    private var house_type: Int = 1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()

        StrictMode.setThreadPolicy(policy)

        files = ArrayList()

        val insertAdsBtn = getView()?.findViewById<Button>(R.id.insertAddBtn)
        val imageAddBtn = getView()?.findViewById<ImageView>(R.id.insertAddView)

        imageAddBtn?.setOnClickListener {
            var intent = Intent()
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, R.string.choseImages.toString())
                , REQUEST_CODE
            )
        }

        insertAdsBtn?.setOnClickListener {
            insertAdd()
        }

        val housetype1Btn = getView()?.findViewById<RadioButton>(R.id.house_type_1)
        val housetype2Btn = getView()?.findViewById<RadioButton>(R.id.house_type_2)

        housetype1Btn?.setOnClickListener {
            onRadioButtonClicked(it)
        }

        housetype2Btn?.setOnClickListener {
            onRadioButtonClicked(it)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val imageView = getView()?.findViewById<ImageView>(R.id.insertAddView)
        val numberImages = getView()?.findViewById<TextView>(R.id.insertAddImageCount)

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            if (data?.clipData != null) {
                var count = data.clipData?.itemCount

                for (i in 0..count!! - 1){
                    var imageUri: Uri = data.clipData?.getItemAt(i)!!.uri
                    file = imageUri
                    files.add(imageUri)
                    Picasso.get().load(imageUri).into(imageView)
                }
            } else if (data?.data != null) {
                var imageUri: Uri = data.data!!
                files.add(imageUri)
                Picasso.get().load(imageUri).into(imageView)
            }
        }
        numberImages?.setText(""+getString(R.string.number_images)+" "+files.size.toString())
    }

    fun insertAdd() {
        val yourAds = YourAds()

        val advertisementName = getView()?.findViewById<EditText>(R.id.insertAddAdvName)
        val location = getView()?.findViewById<EditText>(R.id.insertAddLocation)
        val rooms = getView()?.findViewById<EditText>(R.id.insertAddRooms)
        val bathrooms = getView()?.findViewById<EditText>(R.id.insertAddBathrooms)
        val area = getView()?.findViewById<EditText>(R.id.insertAddArea)
        val rent = getView()?.findViewById<EditText>(R.id.insertAddPrice)
        val constructionYear =  getView()?.findViewById<EditText>(R.id.insertAddConstructionYear)
        val email = getView()?.findViewById<EditText>(R.id.insertAddEmail)
        val contact = getView()?.findViewById<EditText>(R.id.insertAddContact)
        val accessibility = getView()?.findViewById<CheckBox>(R.id.insertAddMobility)

        if (email?.text.isNullOrEmpty()
            || advertisementName?.text.isNullOrEmpty()
            || location?.text.isNullOrEmpty()
            || rooms?.text.isNullOrEmpty()
            || bathrooms?.text.isNullOrEmpty()
            || area?.text.isNullOrEmpty()
            || rent?.text.isNullOrEmpty()
            || constructionYear?.text.isNullOrEmpty()
            || contact?.text.isNullOrEmpty()) {
            Toast.makeText(this.context, R.string.errorFieldsEmpty, Toast.LENGTH_LONG).show()
            return
        }

        sharedPreferences = this.requireActivity().getSharedPreferences("PREFERENCE_AUTH", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", "empty")

        var list : ArrayList<MultipartBody.Part> = ArrayList()

        for (uri: Uri in files){
            list.add(prepareFilePart(uri))
        }

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.insertAdd(
            token!!,
//            list,
            createPartFromString(house_type.toString()),
            createPartFromString(area!!.text.toString().trim()),
            createPartFromString(rooms!!.text.toString().trim()),
            createPartFromString(bathrooms!!.text.toString().trim()),
            createPartFromString(rent!!.text.toString().trim()),
            createPartFromString(location!!.text.toString().trim()),
            createPartFromString(constructionYear!!.text.toString().trim()),
            createPartFromString(accessibility!!.isChecked.toString()),
            createPartFromString(email!!.text.toString().trim()),
            createPartFromString(contact!!.text.toString().trim()),
            createPartFromString(advertisementName!!.text.toString().trim()),
        )

        call.enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(
                call: Call<DefaultResponse>,
                response: Response<DefaultResponse>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@InsertAds.context,
                        R.string.ad_added,
                        Toast.LENGTH_SHORT
                    ).show()

                    this@InsertAds.requireActivity().supportFragmentManager.beginTransaction().apply {
                        replace(R.id.flFragment, yourAds)
                        commit()
                    }
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                Toast.makeText(this@InsertAds.context, R.string.errorSignUp, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked

            when (view.getId()) {
                R.id.house_type_1 ->
                    if (checked) {
                        house_type = 1
                    }
                R.id.house_type_2 ->
                    if (checked) {
                        house_type= 2
                    }
            }
        }
    }

    fun prepareFilePart(uri: Uri) : MultipartBody.Part {
        var file: File = File(uri.path!!)
        var requestFile: RequestBody = RequestBody.create(MediaType.parse("image/*"), file)

        return MultipartBody.Part.createFormData("images", file.name, requestFile)
    }

    fun createPartFromString(stringData: String): RequestBody {
        return RequestBody.create(MultipartBody.FORM, stringData)
    }

    companion object {
        private val MEDIA_TYPE_IMAGE = MediaType.parse("image/*")
    }

}