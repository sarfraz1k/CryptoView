package com.example.cryptoview.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.cryptoview.adapter.MarketAdapter
import com.example.cryptoview.apis.ApiInterface
import com.example.cryptoview.apis.ApiUtilities
import com.example.cryptoview.databinding.FragmentMarketBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import models.CryptoCurrency
import java.util.Locale

class MarketFragment : Fragment() {
    private lateinit var binding: FragmentMarketBinding
    private lateinit var list: List<CryptoCurrency>
    private lateinit var adapter: MarketAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding=FragmentMarketBinding.inflate(layoutInflater)
        list= listOf()
        adapter= MarketAdapter(requireContext(),list,"market")
        binding.currencyRecyclerView.adapter=adapter
        lifecycleScope.launch (Dispatchers.IO){
            val res= ApiUtilities.getInstance().create(ApiInterface::class.java).getMarketData()
            if (res.body()!=null){
                withContext(Dispatchers.Main){
                    list=res.body()!!.data.cryptoCurrencyList
                    adapter.updateData(list)
                    binding.spinKitView.visibility= GONE
                }
            }
        }
        searchCoin()
        return binding.root
    }
    lateinit var searchText:String
    private fun searchCoin() {
        binding.searchEditText.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                searchText= p0.toString().lowercase(Locale.getDefault())
                updateRecyclerView()

            }

        })


    }

    private fun updateRecyclerView() {
        val data= ArrayList<CryptoCurrency>()
        for (item in list){
            val coinName=item.name.lowercase(Locale.getDefault())
            val coinSymbol=item.name.lowercase(Locale.getDefault())
            if (coinName.contains(searchText)||coinSymbol.contains(searchText)){
                data.add(item)
            }
        }
        adapter.updateData(data)
    }


}