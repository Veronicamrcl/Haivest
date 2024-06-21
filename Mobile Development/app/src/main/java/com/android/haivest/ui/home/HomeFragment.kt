package com.android.haivest.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.haivest.R
import com.android.haivest.data.factory.ViewModelFactory
import com.android.haivest.databinding.FragmentHomeBinding
import com.android.haivest.ui.detail.DetailNewsActivity
import com.android.haivest.ui.profile.ProfileActivity

//@Suppress("DEPRECATION")
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var newsAdapter: NewsAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.buttonProfile.setOnClickListener {
            val intent = Intent(requireContext(), ProfileActivity::class.java)
            startActivity(intent)
        }

        val sharedPref = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val username = sharedPref.getString("username", "")

        binding.nameTextView.text = username

        setupRecyclerView()

        viewModel.newsData.observe(viewLifecycleOwner) { news ->
            news?.let {
                newsAdapter.submitList(it.articles)
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
                binding.recyclerViewInsight.visibility = View.GONE
            } else {
                binding.progressBar.visibility = View.GONE
                binding.recyclerViewInsight.visibility = View.VISIBLE
            }
        }

        viewModel.fetchNewsData()

    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter { article ->
            val intent = Intent(requireContext(), DetailNewsActivity::class.java)
            intent.putExtra("article", article)
            startActivity(intent)
        }
        binding.recyclerViewInsight.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = newsAdapter
        }
    }


//    @Deprecated("Deprecated in Java")
//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.main_menu, menu)
//        super.onCreateOptionsMenu(menu, inflater)
//    }
//
//    @Deprecated("Deprecated in Java")
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.account -> {
//                val intent = Intent(requireContext(), ProfileActivity::class.java)
//                startActivity(intent)
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}