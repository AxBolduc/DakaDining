package com.bolducsawka.dakadining.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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
import com.bolducsawka.dakadining.dataobjects.User
import com.bolducsawka.dakadining.navigation.CommonCallbacks
import com.bolducsawka.dakadining.viewmodels.RequestListViewModel
import java.text.SimpleDateFormat
import java.util.*

private const val ARG_USER = "user"
private const val TAG = "RequestsPage"

class RequestsPage : Fragment() {

    private lateinit var user: User
    private var requests: List<Request> = mutableListOf()

    private lateinit var imgSwapPage: ImageView
    private lateinit var imgAdd: ImageView
    private lateinit var imgProfile: ImageView

    private lateinit var requestsRecyclerView: RecyclerView
    private var adapter: RequestsPage.RequestAdapter? = null

    private val requestListViewModel: RequestListViewModel by lazy {
        ViewModelProvider(this).get(RequestListViewModel::class.java)
    }

    private var callbacks: OffersPage.Callbacks? = null
    private var commonCallbacks: CommonCallbacks? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            user = it.getSerializable(ARG_USER) as User
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as OffersPage.Callbacks
        commonCallbacks = context as CommonCallbacks
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

        //Remove the add button if user is the wrong role
        if(user.role == "Seller"){
            imgAdd.visibility = View.INVISIBLE
        }

        imgSwapPage.setOnClickListener {
            callbacks?.swapPages(user, false)
        }
        imgAdd.setOnClickListener {
            callbacks?.onAdd(false, user.userID)
        }


        imgProfile.setOnClickListener {
            //get user data to pass to the profile page
            val userLiveData: LiveData<ResponseObject<User>> = BackendFetcher.get().getUserBySessionID(user.session)
            userLiveData.observe(viewLifecycleOwner, Observer {
                if(it.status == 200) {
                    //success
                    callbacks?.onProfile(it.data)
                }else{
                    //fail
                    Toast.makeText(context, it.data.message, Toast.LENGTH_SHORT).show()
                }
            })
        }

        updateUI()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //setup view model by getting requests and starting observer
        requestListViewModel.getRequests()
        requestListViewModel.requests?.observe(viewLifecycleOwner, Observer {
            if(it.status == 200){
                requests = it.data.requests.filter {
                    //filter requests marked as not filled
                    !it.status
                }
            }

            updateUI()
        })
    }

    private fun updateUI(){
        adapter = RequestAdapter(requests)
        requestsRecyclerView.adapter = adapter
    }

    //*******************
    //RecyclerView Classes
    //*******************

    private inner class RequestHolder(view: View): RecyclerView.ViewHolder(view), View.OnClickListener{
        val txtNumOfSwipes: TextView = itemView.findViewById(R.id.txtNumOfSwipes)
        val txtRequestPrice: TextView = itemView.findViewById(R.id.txtRequestPrice)
        val txtRequestDateTime: TextView = itemView.findViewById(R.id.txtRequestDateTime)

        init {
            itemView.setOnClickListener(this)
        }

        var heldRequest: Request? = null

        override fun onClick(p0: View?) {
            heldRequest?.let { commonCallbacks?.onObjectClick(user, false, it, false) }
        }
    }

    private inner class RequestAdapter(var crimes: List<Request>): RecyclerView.Adapter<RequestHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestHolder {
            val view = layoutInflater.inflate(R.layout.list_item_request, parent, false)
            return RequestHolder(view)
        }

        override fun onBindViewHolder(holder: RequestHolder, position: Int) {
            val request = requests[position]
            val sdf = SimpleDateFormat("MM/dd/yy hh:mm", Locale.getDefault())
            holder.apply{
                txtNumOfSwipes.text = "${request.meals} swipes"
                txtRequestPrice.text = "$${request.price}"
                txtRequestDateTime.text = sdf.format(request.time)

                heldRequest = request

            }
        }

        override fun getItemCount(): Int = requests.size
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
        commonCallbacks = null
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