package com.bolducsawka.dakadining.fragments

import android.content.Context
import android.os.Bundle
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
import com.bolducsawka.dakadining.dataobjects.Request
import com.bolducsawka.dakadining.dataobjects.User
import com.bolducsawka.dakadining.viewmodels.RequestListViewModel

private const val ARG_USER = "user"

class RequestsPage : Fragment() {

    private lateinit var user: User

    private lateinit var imgSwapPage: ImageView
    private lateinit var imgAdd: ImageView
    private lateinit var imgProfile: ImageView

    private lateinit var requestsRecyclerView: RecyclerView
    private var adapter: RequestsPage.RequestAdapter? = null;

    private val requestListViewModel: RequestListViewModel by lazy {
        ViewModelProvider(this).get(RequestListViewModel::class.java)
    }

    private var callbacks: OffersPage.Callbacks? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            user = it.getSerializable(ARG_USER) as User
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as OffersPage.Callbacks
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_requests_page, container, false)

        imgSwapPage = view.findViewById(R.id.imgSwapPage)
        imgAdd = view.findViewById(R.id.imgAdd)
        imgProfile = view.findViewById(R.id.imgProfile)

        requestsRecyclerView = view.findViewById(R.id.offersRecyclerView) as RecyclerView
        requestsRecyclerView.layoutManager = LinearLayoutManager(context)

        if(user.role == "Seller"){
            imgAdd.visibility = View.INVISIBLE
        }

        imgSwapPage.setOnClickListener {
            callbacks?.swapPages(user, false)
        }
        imgAdd.setOnClickListener {
            callbacks?.onAdd(false)
        }
        imgProfile.setOnClickListener {
            //Determine if the user is a seller or buyer
            callbacks?.onProfile(user)
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
                txtNumOfSwipes.setText("${request.swipes.toString()} swipes")
                txtRequestPrice.setText(request.price.toString())
                txtRequestDateTime.setText(request.date.time.toString())

            }
        }

        override fun getItemCount(): Int = requestListViewModel.requests.size
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    companion object {
        @JvmStatic
        fun newInstance(user: User) =
            RequestsPage().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_USER, user)
                }

            }
    }
}