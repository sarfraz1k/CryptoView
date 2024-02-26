package com.example.cryptoview.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.cryptoview.adapter.TopLossGainPagerAdapter
import com.example.cryptoview.adapter.TopMarketAdapter
import com.example.cryptoview.apis.ApiInterface
import com.example.cryptoview.apis.ApiUtilities
import com.example.cryptoview.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment(){
    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentHomeBinding.inflate(layoutInflater)
        getTopCurrencyList()
        setTabLayout()
        return binding.root
    }

    private fun setTabLayout() {
        val adapter=TopLossGainPagerAdapter(this)
        binding.contentViewPager.adapter= adapter
        binding.contentViewPager.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == 0) {
                    binding.topGainIndicator.visibility = VISIBLE
                    binding.topLoseIndicator.visibility = GONE
                }else{
                    binding.topGainIndicator.visibility = GONE
                    binding.topLoseIndicator.visibility = VISIBLE

                }
            }
        })
        TabLayoutMediator(binding.tabLayout,binding.contentViewPager){
            tab,position->
            var title= if (position == 0){
                "Top Gainers"
            }else{
                "Top Losers"
            }
            tab.text = title
        }.attach()

    }

    private fun getTopCurrencyList(){
        lifecycleScope.launch(Dispatchers.IO){
            val res=ApiUtilities.getInstance().create(ApiInterface::class.java).getMarketData()
            withContext(Dispatchers.Main){
                    binding.topCurrencyRecyclerView.adapter= TopMarketAdapter(requireContext(),res.body()!!.data.cryptoCurrencyList)

            }


        }

    }
}