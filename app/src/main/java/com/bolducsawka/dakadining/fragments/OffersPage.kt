package com.bolducsawka.dakadining.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bolducsawka.dakadining.R
import com.bolducsawka.dakadining.dataobjects.Offer
import com.bolducsawka.dakadining.dataobjects.Request
import com.bolducsawka.dakadining.viewmodels.OfferListViewModel


class OffersPage : Fragment() {

    private lateinit var offersRecyclerView: RecyclerView
    private var adapter: OffersPage.OfferAdapter? = null;

    private val offerListViewModel: OfferListViewModel by lazy {
        ViewModelProvider(this).get(OfferListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_offers_page, container, false)

        offersRecyclerView = view.findViewById(R.id.offersRecyclerView) as RecyclerView
        offersRecyclerView.layoutManager = LinearLayoutManager(context)

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

    companion object {
        @JvmStatic
        fun newInstance() =
            OffersPage().apply {

            }
    }
}