package com.bolducsawka.dakadining.fragments

import android.content.Context
import android.os.Bundle
import android.telecom.Call
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bolducsawka.dakadining.R
import com.bolducsawka.dakadining.dataobjects.Offer
import com.bolducsawka.dakadining.dataobjects.Request
import com.bolducsawka.dakadining.dataobjects.User
import com.bolducsawka.dakadining.viewmodels.OfferListViewModel

private const val ARG_USER = "user"

class OffersPage : Fragment() {

    interface Callbacks{
        fun swapPages(user: User, fromOffers: Boolean)
        fun onAdd(fromOffers:Boolean)
        fun onProfile(user: User)
    }

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            user = it.getSerializable(ARG_USER)as User
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks
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

        if(user.role != "Buyer"){
            imgAdd.visibility = View.INVISIBLE
        }

        imgSwapPage.setOnClickListener {
            callbacks?.swapPages(user, true)
        }
        imgAdd.setOnClickListener {
            callbacks?.onAdd(true)
        }
        imgProfile.setOnClickListener {
            //Determine if user is a seller or not
            callbacks?.onProfile(user)
        }

        updateUI()

        return view
    }

    private fun updateUI(){
        adapter = OfferAdapter(offerListViewModel.offers)
        offersRecyclerView.adapter = adapter
    }

    private inner class OfferHolder(view: View): RecyclerView.ViewHolder(view){
        val txtNumOfSwipes: TextView = itemView.findViewById(R.id.txtNumOfSwipes)
        val txtRequestPrice: TextView = itemView.findViewById(R.id.txtRequestPrice)
        val txtRequestDateTime: TextView = itemView.findViewById(R.id.txtRequestDateTime)
    }

    private inner class OfferAdapter(var offers: List<Offer>): RecyclerView.Adapter<OfferHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferHolder {
            val view = layoutInflater.inflate(R.layout.list_item_request, parent, false)
            return OfferHolder(view)
        }

        override fun onBindViewHolder(holder: OfferHolder, position: Int) {
            val request = offerListViewModel.offers[position]
            holder.apply{
                txtNumOfSwipes.setText("${request.swipes.toString()} swipes")
                txtRequestPrice.setText(request.price.toString())
                txtRequestDateTime.setText("")

            }
        }

        override fun getItemCount(): Int = offerListViewModel.offers.size
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
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