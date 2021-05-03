package fr.isen.brunelleservat.foodstyle2

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import fr.isen.brunelleservat.foodstyle2.databinding.ActivityBLEDeviceBinding
import fr.isen.brunelleservat.foodstyle2.model.BLEConnexionState

class BLEDeviceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBLEDeviceBinding
    var bluetoothGatt: BluetoothGatt? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBLEDeviceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val device = intent.getParcelableExtra<BluetoothDevice>("ble_device")
        binding.deviceName.text = device?.name ?: "Appareil inconnu"
        binding.deviceStatus.text = "Status: en cours de connexion"

        connectToDevice(device)
    }

    private fun connectToDevice(device: BluetoothDevice?){
        bluetoothGatt = device?.connectGatt(this, false, object: BluetoothGattCallback() {
            override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int){
                super.onConnectionStateChange(gatt, status, newState)


            }

            override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
                super.onServicesDiscovered(gatt, status)
            }

            override fun onCharacteristicRead(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?, status: Int) {
                super.onCharacteristicRead(gatt, characteristic, status)
            }

        })
    }
    private fun connectionStateChange(newState: Int, gatt: BluetoothGatt?){
        BLEConnexionState.getBLEConnexionFromState(newState)?.let {
            runOnUiThread { binding.deviceStatus.text = getString(R.string.ble_device_status, getString(it.text)) }

        }
    }
}