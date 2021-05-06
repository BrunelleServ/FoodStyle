package fr.isen.brunelleservat.foodstyle2


import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanResult
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.isen.brunelleservat.foodstyle2.databinding.CellBluetoothBinding


class BLEAdapter(private val listDevice: MutableList<ScanResult>, val deviceClickCall: (BluetoothDevice)-> Unit) :
    RecyclerView.Adapter<BLEAdapter.DeviceViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DeviceViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CellBluetoothBinding.inflate(layoutInflater)
        return DeviceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        holder.deviceAddress.text = listDevice[position].device.address
        holder.deviceTitre.text = listDevice[position].device.name
        holder.layout.setOnClickListener{
            deviceClickCall (listDevice[position].device)
        }
    }

    fun addDevice(appareilData: ScanResult) {
        if (!listDevice.contains(appareilData))
            listDevice.add(appareilData)
    }

    override fun getItemCount(): Int = listDevice.size

    class DeviceViewHolder(binding: CellBluetoothBinding) : RecyclerView.ViewHolder(binding.root) {
        val deviceTitre: TextView = binding.titreDevice
        val deviceAddress: TextView = binding.adresseDevice
        val layout = binding.cellDevice
    }
}