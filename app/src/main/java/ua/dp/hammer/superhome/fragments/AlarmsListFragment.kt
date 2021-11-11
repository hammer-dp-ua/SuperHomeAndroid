package ua.dp.hammer.superhome.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import ua.dp.hammer.superhome.adapters.AlarmsListAdapter
import ua.dp.hammer.superhome.databinding.FragmentAlarmsListBinding
import ua.dp.hammer.superhome.models.AlarmsViewModel

class AlarmsListFragment : Fragment() {
    private val viewModel by activityViewModels<AlarmsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View {
        val binding = FragmentAlarmsListBinding.inflate(inflater, container, false)
        val adapter = AlarmsListAdapter(this)

        subscribeUi(adapter)
        binding.alarmsList.adapter = adapter

        return binding.root
    }

    private fun subscribeUi(adapter: AlarmsListAdapter) {
        viewModel.streetMotionDetectors.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }
}