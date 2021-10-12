package com.bolducsawka.dakadining.fragments

import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
import com.bolducsawka.dakadining.api.responseobjects.MealsUpdateReponse
import com.bolducsawka.dakadining.api.responseobjects.ResponseObject
import com.bolducsawka.dakadining.dataobjects.Offer
import com.bolducsawka.dakadining.dataobjects.User
import com.bolducsawka.dakadining.navigation.CommonCallbacks
import com.bolducsawka.dakadining.viewmodels.OfferListViewModel
import android.util.Base64
import com.bolducsawka.dakadining.api.requestobjects.UpdatePictureRequest
import com.bolducsawka.dakadining.api.responseobjects.UpdatePictureResponse
import java.io.ByteArrayOutputStream
import android.graphics.BitmapFactory
import androidx.lifecycle.MutableLiveData
import java.util.*

private const val ARG_USER = "user"
private const val TAG = "SellerProfilePage"

class SellerProfilePage : Fragment(){

    private lateinit var user: User
    private var offers: List<Offer> = mutableListOf()

    private var profilePic: MutableLiveData<String> = MutableLiveData()

    private lateinit var txtSellerName: TextView
    private lateinit var txtNumSwipes: TextView
    private lateinit var btnLogout: ImageView
    private lateinit var btnBack: ImageView
    private lateinit var btnUseSwipe: Button
    private lateinit var imgProfilePic: ImageView

    private lateinit var offersRecyclerView: RecyclerView
    private var adapter: OfferAdapter? = null;

    private val offerListViewModel: OfferListViewModel by lazy {
        ViewModelProvider(this).get(OfferListViewModel::class.java)
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


        val view = inflater.inflate(R.layout.fragment_profile_seller_page, container, false)

        btnLogout = view.findViewById(R.id.btnLogout)
        btnBack = view.findViewById(R.id.imgBack)
        btnUseSwipe = view.findViewById(R.id.btnUseSwipe)
        txtSellerName = view.findViewById(R.id.txtSellerName)
        txtNumSwipes = view.findViewById(R.id.txtNumSwipes)
        imgProfilePic = view.findViewById(R.id.imgProfilePic)

        offersRecyclerView = view.findViewById(R.id.offeringsRecyclerView) as RecyclerView
        offersRecyclerView.layoutManager = LinearLayoutManager(context)

        btnLogout.setOnClickListener {
            callbacks?.onLogout()
        }

        btnBack.setOnClickListener {
            callbacks?.onBack()
        }

        btnUseSwipe.setOnClickListener {
            //update UI
            if (user.meals != 0) {
                //update database
                val updateMealsLiveData: LiveData<ResponseObject<MealsUpdateReponse>> =
                    BackendFetcher.get().updateMealsBySessionID(user.session, false, 1)
                updateMealsLiveData.observe(viewLifecycleOwner, Observer {
                    if(it.status == 200){
                        user.meals = it.data.meals
                        updateUI()
                    }else{
                        Toast.makeText(context, it.data.message, Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(context, "You're out of swipes", Toast.LENGTH_SHORT).show()
            }
        }

        imgProfilePic.setOnClickListener {
            dispatchTakePictureIntent()

        }

        profilePic.observe(viewLifecycleOwner, Observer {
            user.profilePic = it
        })


        updateUI()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        offerListViewModel.getOffers()
        offerListViewModel.offers?.observe(viewLifecycleOwner, Observer {
            if(it.status == 200){
                offers = it.data.offers.filter {
                    it.offerer == user.userID && !it.status
                }
            }

            updateUI()
        })
    }

    private fun updateUI(){
        adapter = OfferAdapter(offers)
        offersRecyclerView.adapter = adapter

        txtSellerName.setText("${user.firstName} ${user.lastName}")
        txtNumSwipes.setText("${user.meals} swipes left")

        user.profilePic?.let {
            val decodedString: ByteArray = Base64.decode(it, Base64.DEFAULT)
            val decodedByte: Bitmap= BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
            imgProfilePic.setImageBitmap(decodedByte)
        }
    }

    private inner class OfferHolder(view: View): RecyclerView.ViewHolder(view), View.OnClickListener{
        val txtNumOfSwipes: TextView = itemView.findViewById(R.id.txtNumOfSwipes)
        val txtRequestPrice: TextView = itemView.findViewById(R.id.txtRequestPrice)
        val txtRequestDateTime: TextView = itemView.findViewById(R.id.txtRequestDateTime)

        init {
            itemView.setOnClickListener(this)
        }

        var heldOffer: Offer? = null

        override fun onClick(p0: View?) {
            heldOffer?.let { callbacks?.onObjectClick(user, true, it, true) }
        }
    }

    private inner class OfferAdapter(var crimes: List<Offer>): RecyclerView.Adapter<OfferHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferHolder {
            val view = layoutInflater.inflate(R.layout.list_item_request, parent, false)
            return OfferHolder(view)
        }

        override fun onBindViewHolder(holder: OfferHolder, position: Int) {
            val offer = offers[position]
            holder.apply{
                txtNumOfSwipes.setText("${offer.meals.toString()} swipes")
                txtRequestPrice.setText(offer.price.toString())
                txtRequestDateTime.visibility = View.INVISIBLE

                heldOffer = offer
            }
        }

        override fun getItemCount(): Int = offers.size
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    companion object{
        fun newInstance(user: User) =
            SellerProfilePage().apply {
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
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            imgProfilePic.setImageBitmap(imageBitmap)

        // convert bitmap to base 64
            val byteArrayOutputStream = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            val byteArray: ByteArray = byteArrayOutputStream.toByteArray()

            val encoded: String = Base64.encodeToString(byteArray, Base64.DEFAULT)
            profilePic.value = encoded

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