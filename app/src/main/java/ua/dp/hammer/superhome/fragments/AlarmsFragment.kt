package ua.dp.hammer.superhome.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import ua.dp.hammer.superhome.databinding.FragmentAlarmsBinding
import ua.dp.hammer.superhome.models.AlarmsViewModel

class AlarmsFragment : Fragment() {
    private val viewModel by activityViewModels<AlarmsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View {
        val binding = FragmentAlarmsBinding.inflate(inflater, container, false)

        context ?: return binding.root

        return binding.root
    }
}