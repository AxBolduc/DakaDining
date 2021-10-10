package com.bolducsawka.dakadining.fragments

import android.content.Context
import android.os.Bundle
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

private const val ARG_USER = "user"

class SellerProfilePage : Fragment(){

    private lateinit var user: User

    private lateinit var txtSellerName: TextView
    private lateinit var txtNumSwipes: TextView
    private lateinit var btnLogout: ImageView
    private lateinit var btnBack: ImageView
    private lateinit var btnUseSwipe: Button

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
        btnBack = view.findViewById(R.id.btnBack)
        btnUseSwipe = view.findViewById(R.id.btnUseSwipe)
        txtSellerName = view.findViewById(R.id.txtSellerName)
        txtNumSwipes = view.findViewById(R.id.txtNumSwipes)


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
                    user.meals = it.data.meals
                    updateUI()
                })
            } else {
                Toast.makeText(context, "You're out of swipes", Toast.LENGTH_SHORT).show()
            }
        }



        updateUI()

        return view
    }

    private fun updateUI(){
        adapter = OfferAdapter(offerListViewModel.offers)
        offersRecyclerView.adapter = adapter

        txtSellerName.setText("${user.firstName} ${user.lastName}")
        txtNumSwipes.setText("${user.meals} swipes left")
    }

    private inner class OfferHolder(view: View): RecyclerView.ViewHolder(view){
        val txtNumOfSwipes: TextView = itemView.findViewById(R.id.txtNumOfSwipes)
        val txtRequestPrice: TextView = itemView.findViewById(R.id.txtRequestPrice)
        val txtRequestDateTime: TextView = itemView.findViewById(R.id.txtRequestDateTime)
    }

    private inner class OfferAdapter(var crimes: List<Offer>): RecyclerView.Adapter<OfferHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferHolder {
            val view = layoutInflater.inflate(R.layout.list_item_request, parent, false)
            return OfferHolder(view)
        }

        override fun onBindViewHolder(holder: OfferHolder, position: Int) {
            val offer = offerListViewModel.offers[position]
            holder.apply{
                txtNumOfSwipes.setText("${offer.swipes.toString()} swipes")
                txtRequestPrice.setText(offer.price.toString())
                txtRequestDateTime.visibility = View.INVISIBLE
            }
        }

        override fun getItemCount(): Int = offerListViewModel.offers.size
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

}