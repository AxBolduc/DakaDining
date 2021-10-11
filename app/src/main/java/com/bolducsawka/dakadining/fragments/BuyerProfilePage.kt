package com.bolducsawka.dakadining.fragments

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bolducsawka.dakadining.R
import com.bolducsawka.dakadining.api.BackendFetcher
import com.bolducsawka.dakadining.api.requestobjects.UpdatePictureRequest
import com.bolducsawka.dakadining.api.responseobjects.ResponseObject
import com.bolducsawka.dakadining.api.responseobjects.UpdatePictureResponse
import com.bolducsawka.dakadining.dataobjects.Request
import com.bolducsawka.dakadining.dataobjects.User
import com.bolducsawka.dakadining.navigation.CommonCallbacks
import com.bolducsawka.dakadining.viewmodels.RequestListViewModel
import java.io.ByteArrayOutputStream

private const val ARG_USER = "user"

class BuyerProfilePage : Fragment(){

    private lateinit var user: User

    private lateinit var btnLogout: ImageView
    private lateinit var btnBack: ImageView

    private lateinit var txtBuyerName: TextView

    private lateinit var imgProfilePic: ImageView

    private lateinit var requestsRecyclerView: RecyclerView
    private var adapter: RequestAdapter? = null;

    private val requestListViewModel: RequestListViewModel by lazy {
        ViewModelProvider(this).get(RequestListViewModel::class.java)
    }

    private var callbacks: CommonCallbacks? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            user = it.getSerializable(ARG_USER) as User
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as CommonCallbacks
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile_buyer_page, container, false)

        btnLogout = view.findViewById(R.id.btnLogout)
        btnBack = view.findViewById(R.id.btnBack)
        txtBuyerName = view.findViewById(R.id.txtBuyerName)
        imgProfilePic = view.findViewById(R.id.imgProfilePic)

        user.profilePic?.let {
            val decodedString: ByteArray = Base64.decode(it, Base64.DEFAULT)
            val decodedByte: Bitmap= BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
            imgProfilePic.setImageBitmap(decodedByte)
        }

        requestsRecyclerView = view.findViewById(R.id.requestRecyclerView) as RecyclerView
        requestsRecyclerView.layoutManager = LinearLayoutManager(context)

        btnLogout.setOnClickListener {
            callbacks?.onLogout()
        }
        btnBack.setOnClickListener {
            callbacks?.onBack()
        }

        //Populate Name fields
        txtBuyerName.setText("${user.firstName} ${user.lastName}")


        imgProfilePic.setOnClickListener {
            dispatchTakePictureIntent()
        }


        updateUI()

        return view
    }

    private fun updateUI(){
        adapter = RequestAdapter(requestListViewModel.requests)
        requestsRecyclerView.adapter = adapter
    }

    private inner class RequestHolder(view: View): RecyclerView.ViewHolder(view){
        val txtNumOfSwipes: TextView = itemView.findViewById(R.id.txtNumOfSwipes)
        val txtRequestPrice: TextView = itemView.findViewById(R.id.txtRequestPrice)
        val txtRequestDateTime: TextView = itemView.findViewById(R.id.txtRequestDateTime)
    }

    private inner class RequestAdapter(var crimes: List<Request>): RecyclerView.Adapter<RequestHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestHolder {
            val view = layoutInflater.inflate(R.layout.list_item_request, parent, false)
            return RequestHolder(view)
        }

        override fun onBindViewHolder(holder: RequestHolder, position: Int) {
            val request = requestListViewModel.requests[position]
            holder.apply{
                txtNumOfSwipes.setText("${request.meals.toString()} swipes")
                txtRequestPrice.setText(request.price.toString())
                txtRequestDateTime.setText(request.time.time.toString())

            }
        }

        override fun getItemCount(): Int = requestListViewModel.requests.size
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    companion object{
        fun newInstance(user: User) =
            BuyerProfilePage().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_USER, user)
                }
            }
    }


    val REQUEST_IMAGE_CAPTURE = 1

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch(e: ActivityNotFoundException) {
            // display error
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            imgProfilePic.setImageBitmap(imageBitmap)

            // convert bitmap to base 64
            val byteArrayOutputStream = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.JPEG,100, byteArrayOutputStream)
            val byteArray: ByteArray = byteArrayOutputStream.toByteArray()

            val encoded: String = Base64.encodeToString(byteArray, Base64.DEFAULT)
            user.profilePic = encoded

            val profilePicLiveData: LiveData<ResponseObject<UpdatePictureResponse>> = BackendFetcher.get().updateProfilePicture(
                UpdatePictureRequest(user.session, encoded)
            )

            profilePicLiveData.observe(viewLifecycleOwner, Observer {
                if(it.status == 200){
                    Toast.makeText(context, it.data.message, Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(context, it.data.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

}