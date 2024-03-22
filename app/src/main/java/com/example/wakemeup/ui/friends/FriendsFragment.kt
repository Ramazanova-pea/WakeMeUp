package com.example.wakemeup.ui.friends

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wakemeup.R
import com.example.wakemeup.databinding.FragmentFriendsBinding
import com.example.wakemeup.domain.FriendModel
import java.util.Calendar

class FriendsFragment : Fragment() {

    private var _binding: FragmentFriendsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val friendsViewModel =
            ViewModelProvider(this)[FriendsViewModel::class.java]

        _binding = FragmentFriendsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val friendsAdapter = FriendsAdapter(
            arrayListOf(
                FriendModel(
                    name="Daniel",
                    phoneNumber="1234567890",
                    isAwake=true,
                    wakingTime= Calendar.getInstance(),
                    profilePic= resources.getDrawable(R.drawable.ic_launcher_foreground),
                    notes="This is a note"
                ),
                FriendModel(
                    name="Peter",
                    phoneNumber="1234567890",
                    isAwake=false,
                    wakingTime= Calendar.getInstance(),
                    profilePic= resources.getDrawable(R.drawable.account_circle_24px),
                    notes="This is a note"
                ),
                FriendModel(
                    name="Marina",
                    phoneNumber="1234567890",
                    isAwake=true,
                    wakingTime= Calendar.getInstance(),
                    profilePic= resources.getDrawable(R.drawable.ic_launcher_foreground),
                    notes="This is a note"
                ),
                FriendModel(
                    name="hhhhhhhhhhhhhhhhh",
                    phoneNumber="1234567890",
                    isAwake=true,
                    wakingTime= Calendar.getInstance(),
                    profilePic= resources.getDrawable(R.drawable.ic_launcher_foreground),
                    notes="This is a note"
                ),
            )
        )

        binding.friendsRecyclerView.adapter = friendsAdapter
        binding.friendsRecyclerView.layoutManager = LinearLayoutManager(context)

        // Example of a LiveData object
//        val textView: TextView = binding.textFriends
//        friendsViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}