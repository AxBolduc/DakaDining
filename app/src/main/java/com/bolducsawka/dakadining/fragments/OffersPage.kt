package com.bolducsawka.dakadining.fragments

import android.content.Context
import android.os.Bundle
import android.telecom.Call
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bolducsawka.dakadining.R
import com.bolducsawka.dakadining.api.BackendFetcher
import com.bolducsawka.dakadining.api.responseobjects.ResponseObject
import com.bolducsawka.dakadining.dataobjects.Offer
import com.bolducsawka.dakadining.dataobjects.Request
import com.bolducsawka.dakadining.dataobjects.SwipeObject
import com.bolducsawka.dakadining.dataobjects.User
import com.bolducsawka.dakadining.navigation.CommonCallbacks
import com.bolducsawka.dakadining.viewmodels.OfferListViewModel

private const val ARG_USER = "user"
private const val TAG = "OffersPage"

class OffersPage : Fragment() {

    interface Callbacks{
        fun swapPages(user: User, fromOffers: Boolean)
        fun onAdd(fromOffers:Boolean, userID: String)
        fun onProfile(user: User)
    }

    private var offers: List<Offer> = mutableListOf()

    private lateinit var user: User

    private lateinit var imgSwapPage: ImageView
    private lateinit var imgAdd: ImageView
    private lateinit var imgProfile: ImageView

    private lateinit var offersRecyclerView: RecyclerView
    private var adapter: OffersPage.OfferAdapter? = null;

    private val offerListViewModel: OfferListViewModel by lazy {
        ViewModelProvider(this).get(OfferListViewModel::class.java)
    }

    private var callbacks: Callbacks? = null
    private var commonCallbacks: CommonCallbacks? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            user = it.getSerializable(ARG_USER)as User
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks
        commonCallbacks = context as CommonCallbacks
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_offers_page, container, false)

        imgSwapPage = view.findViewById(R.id.imgSwapPage)
        imgAdd = view.findViewById(R.id.imgAdd)
        imgProfile = view.findViewById(R.id.imgProfile)

        offersRecyclerView = view.findViewById(R.id.offersRecyclerView) as RecyclerView
        offersRecyclerView.layoutManager = LinearLayoutManager(context)

        if(user.role == "Buyer"){
            imgAdd.visibility = View.INVISIBLE
        }

        imgSwapPage.setOnClickListener {
            callbacks?.swapPages(user, true)
        }
        imgAdd.setOnClickListener {
            callbacks?.onAdd(true, user.userID)
        }
        imgProfile.setOnClickListener {
            //Determine if user is a seller or not
            val userLiveData: LiveData<ResponseObject<User>> = BackendFetcher.get().getUserBySessionID(user.session)
            userLiveData.observe(viewLifecycleOwner, Observer {
                if(it.status == 200) {
                    callbacks?.onProfile(it.data)
                }
            })
        }

        updateUI()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        offerListViewModel.getOffers()
        offerListViewModel.offers?.observe(viewLifecycleOwner, Observer {
            if(it.status == 200){
                offers = it.data.offers.filter {
                    !it.status
                }
            }

            updateUI()
        })
    }

    private fun updateUI(){
        adapter = OfferAdapter(offers)
        offersRecyclerView.adapter = adapter
    }

    private inner class OfferHolder(view: View): RecyclerView.ViewHolder(view), View.OnClickListener{
        val txtNumOfSwipes: TextView = itemView.findViewById(R.id.txtNumOfSwipes)
        val txtRequestPrice: TextView = itemView.findViewById(R.id.txtRequestPrice)
        val txtRequestDateTime: TextView = itemView.findViewById(R.id.txtRequestDateTime)

        init {
            itemView.setOnClickListener(this)
        }

        var heldOffer: Offer? = null;


        override fun onClick(p0: View?) {
            if(user.role == "Buyer"){
                heldOffer?.let { commonCallbacks?.onObjectClick(user, true, it, false) }
            }
        }
    }

    private inner class OfferAdapter(var offers: List<Offer>): RecyclerView.Adapter<OfferHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferHolder {
            val view = layoutInflater.inflate(R.layout.list_item_request, parent, false)
            return OfferHolder(view)
        }

        override fun onBindViewHolder(holder: OfferHolder, position: Int) {
            val offer = offers[position]
            holder.apply{
                txtNumOfSwipes.setText("${offer.meals.toString()} swipes")
                txtRequestPrice.setText("$${offer.price.toString()}")
                txtRequestDateTime.setText("")
                heldOffer = offer
            }
        }

        override fun getItemCount(): Int = offers.size
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
        commonCallbacks = null
    }

    companion object {
        @JvmStatic
        fun newInstance(user: User) =
            OffersPage().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_USER, user)
                }
            }
    }
}