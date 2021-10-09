package com.bolducsawka.dakadining.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bolducsawka.dakadining.R
import com.bolducsawka.dakadining.dataobjects.Request
import com.bolducsawka.dakadining.viewmodels.RequestListViewModel

class BuyerProfilePage : Fragment(){

    private lateinit var requestsRecyclerView: RecyclerView
    private var adapter: RequestAdapter? = null;

    private val requestListViewModel: RequestListViewModel by lazy {
        ViewModelProvider(this).get(RequestListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile_buyer_page, container, false)

        requestsRecyclerView = view.findViewById(R.id.requestsRecyclerView) as RecyclerView
        requestsRecyclerView.layoutManager = LinearLayoutManager(context)

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
                txtNumOfSwipes.setText("${request.swipes.toString()} swipes")
                txtRequestPrice.setText(request.price.toString())
                txtRequestDateTime.setText(request.date.time.toString())

            }
        }

        override fun getItemCount(): Int = requestListViewModel.requests.size
    }

    companion object{
        fun newInstance() =
            BuyerProfilePage().apply {

            }
    }

}