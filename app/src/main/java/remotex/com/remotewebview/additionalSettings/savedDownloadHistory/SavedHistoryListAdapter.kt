package remotex.com.remotewebview.additionalSettings.savedDownloadHistory

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import remotex.com.remotewebview.R
import remotex.com.remotewebview.databinding.ItemSavedHistoryRowsBinding


class SavedHistoryListAdapter(
    private val listener: OnItemClickListener,
  /// private val context: Context
): RecyclerView.Adapter<SavedHistoryListAdapter.MyViewHolder>() {

    private var userList = emptyList<User>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemSavedHistoryRowsBinding.bind(
            LayoutInflater.from(parent.context).inflate(R.layout.item_saved_history_rows, parent, false)
        )

        return MyViewHolder(binding)
    }

    interface OnItemClickListener{
        fun onItemClicked(photo: User)
    }

    inner class MyViewHolder(private val binding: ItemSavedHistoryRowsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val fileNameTextView = binding.textCloDemoFolder


        init {
            binding.textCloDemoFolder.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = userList[position]
                   listener.onItemClicked(item)

                }
            }
        }

    }

    override fun getItemCount(): Int {
        return userList.size
    }


    @SuppressLint("Range")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = userList[position]


        val CLO = currentItem.CLO
        val DEMO  = currentItem.DEMO
        val Editurl = currentItem.EditUrl
        val title = "$CLO/$DEMO"
        if (CLO.isNotEmpty() || DEMO.isNotEmpty() && Editurl.isEmpty()){
            holder.fileNameTextView.text = title
        }else{
            holder.fileNameTextView.text = Editurl
        }





        }


    @SuppressLint("NotifyDataSetChanged")
    fun setData(user: List<User>){
        this.userList = user
        notifyDataSetChanged()
    }



}
