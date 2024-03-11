package com.example.cryptoview.fragment
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.cryptoview.R
import com.example.cryptoview.databinding.FragmentDetailsBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import models.CryptoCurrency

class DetailsFragment:Fragment(){
   private lateinit var binding:FragmentDetailsBinding


    private val item:DetailsFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater,container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentDetailsBinding.inflate(layoutInflater)
        val data : CryptoCurrency= item.data!!
        setUpDetails(data)
        loadChart(data)
        setButtonOnClick(data)
        addToWatchList(data)

        binding.backStackButton.setOnClickListener {
            requireActivity().onBackPressed()
        }
        return binding.root

    }
    var watchList: ArrayList<String>?= null
    var watchListIsChecked = false


    private fun addToWatchList(data: CryptoCurrency){
        readData()

        watchListIsChecked = if(watchList!!.contains(data.symbol)){
            binding.addWatchlistButton.setImageResource(R.drawable.ic_star)
            true

        }else{
            binding.addWatchlistButton.setImageResource(R.drawable.ic_star_outline)
            false

        }
        binding.addWatchlistButton.setOnClickListener {
            watchListIsChecked =
                if (!watchListIsChecked){
                    if(!watchList!!.contains(data.symbol)){
                        watchList!!.add(data.symbol)
                    }
                    storeData()
                    binding.addWatchlistButton.setImageResource(R.drawable.ic_star)
                    true
                }else{
                    binding.addWatchlistButton.setImageResource(R.drawable.ic_star_outline)
                    watchList!!.remove(data.symbol)
                    storeData()
                    false
                }
        }
    }
    private fun storeData(){
        val sharedPreferences = requireContext().getSharedPreferences("watchlist", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(watchList)
        editor.putString("watchlist",json)
        editor.apply()
    }

    private fun readData() {
        val sharedPreferences = requireContext().getSharedPreferences("watchlist", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("watchlist",ArrayList<String>().toString())
        val type = object : TypeToken<ArrayList<String>>(){}.type

        watchList = gson.fromJson(json,type)



    }

    private fun setButtonOnClick(item: CryptoCurrency) {
        val oneMonth = binding.button
        val oneWeak = binding.button1
        val oneDay = binding.button2
        val fourHour = binding.button3
        val oneHour = binding.button4
        val fifteenMinute = binding.button5
        val clickListener = View.OnClickListener {
            when(it.id){
                fifteenMinute.id->loadChartData(it,"15",item,oneDay,oneMonth,oneWeak,fourHour,oneHour)
                oneHour.id->loadChartData(it,"1H",item,oneDay,oneMonth,oneWeak,fourHour,fifteenMinute)
                fourHour.id->loadChartData(it,"4H",item,oneDay,oneMonth,oneWeak,fifteenMinute,oneHour)
                oneDay.id->loadChartData(it,"D",item,fifteenMinute,oneMonth,oneWeak,fourHour,oneHour)
                oneWeak.id->loadChartData(it,"w",item,oneDay,oneMonth,fifteenMinute,fourHour,oneHour)
                oneMonth.id->loadChartData(it,"M",item,oneDay,fifteenMinute,oneWeak,fourHour,oneHour)
            }
        }
        fifteenMinute.setOnClickListener(clickListener)
        oneHour.setOnClickListener(clickListener)
        fourHour.setOnClickListener(clickListener)
        oneDay.setOnClickListener(clickListener)
        oneWeak.setOnClickListener(clickListener)
        oneMonth.setOnClickListener(clickListener)

    }

    private fun loadChartData(
        it: View?,
        s: String,
        item: CryptoCurrency,
        oneDay: AppCompatButton,
        oneMonth: AppCompatButton,
        oneWeak: AppCompatButton,
        fourHour: AppCompatButton,
        oneHour: AppCompatButton
    ) {
        disableButton(oneDay,oneMonth,oneWeak,fourHour,oneHour,)
        it!!.setBackgroundResource(R.drawable.active_button)
        binding.detaillChartWebView.settings.javaScriptEnabled=true
        binding.detaillChartWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        binding.detaillChartWebView.loadUrl(
            "https://s.tradingview.com/widgetembed/?frameElementId=tradingView_76d87&symbol=" + item.symbol
                .toString() + "USD&interval="+s+"&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg="+
                    "F1F3F6&studies=[]&hideideas=1&theme=Dark&style=1&timezone=Etc%2FUTC&studies_overrides={}&overrides={}&enabled_features=" +
                    "[]&disabled_features=[]&locale=en&utm_source=coinmarketcap.com&utm_medium=widget&utm_campaign=chart&utm_term=BTCUSDT"


        )

    }

    private fun disableButton(oneDay: AppCompatButton, oneMonth: AppCompatButton, oneWeak: AppCompatButton, fourHour: AppCompatButton, oneHour: AppCompatButton) {
        oneDay.background=null
        oneMonth.background=null
        oneWeak.background=null
        fourHour.background=null
        oneHour.background=null

    }

    private fun loadChart(item: CryptoCurrency){

        binding.detaillChartWebView.settings.javaScriptEnabled=true
        binding.detaillChartWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        binding.detaillChartWebView.loadUrl(
            "https://s.tradingview.com/widgetembed/?frameElementId=tradingView_76d87&symbol=" + item.symbol
                .toString() + "USD&interval=D&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg="+
                    "F1F3F6&studies=[]&hideideas=1&theme=Dark&style=1&timezone=Etc%2FUTC&studies_overrides={}&overrides={}&enabled_features=" +
                    "[]&disabled_features=[]&locale=en&utm_source=coinmarketcap.com&utm_medium=widget&utm_campaign=chart&utm_term=BTCUSDT"
        )

    }
    private fun setUpDetails(data:CryptoCurrency){
        binding.detailSymbolTextView.text=data.symbol

        Glide.with(requireContext()).load(
            "https://s2.coinmarketcap.com/static/img/coins/64x64/" +data.id +".png"

        ).thumbnail(Glide.with(requireContext()).load(R.drawable.spinner))
            .into(binding.detailImageView)

        binding.detailPriceTextView.text="${String.format("$%.4f",data.quotes[0].price)}"

        if(data.quotes[0].percentChange24h>0) {
            binding.detailChangeTextView.setTextColor(requireContext().resources.getColor(R.color.green))
            binding.detailChangeImageView.setImageResource(R.drawable.ic_caret_up)
            binding.detailChangeTextView.text = "+${String.format("%.02f",data.quotes[0].percentChange24h)}%"
        }else{
            binding.detailChangeImageView.setImageResource(R.drawable.ic_caret_down)
            binding.detailChangeTextView.setTextColor(requireContext().resources.getColor(R.color.red))
            binding.detailChangeTextView.text = "${String.format("%.02f",data.quotes[0].percentChange24h)}%"
        }
        
    }
}