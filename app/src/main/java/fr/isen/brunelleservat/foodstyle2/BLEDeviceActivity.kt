package fr.isen.brunelleservat.foodstyle2

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import fr.isen.brunelleservat.foodstyle2.databinding.ActivityBLEDeviceBinding
import fr.isen.brunelleservat.foodstyle2.model.BLEConnexionState
import fr.isen.brunelleservat.foodstyle2.model.BLEService

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
        bluetoothGatt = device?.connectGatt(this, true, bleGatt )
        binding.adressBLE.text = device?.address
        connectToDevice(device)
        bluetoothGatt?.connect()
    }

    private fun connectToDevice(device: BluetoothDevice?){
        bluetoothGatt = device?.connectGatt(
                this,
                false,
                bleGatt)
    }
    private val bleGatt = object : BluetoothGattCallback() {
            override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int){
                super.onConnectionStateChange(gatt, status, newState)
                connectionStateChange(newState, gatt)


            }

            override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
                super.onServicesDiscovered(gatt, status)
                runOnUiThread {
                    binding.bleDeviceRecycler.adapter = BLEServiceAdapter(
                            gatt,
                            gatt?.services?.map {
                                BLEService(
                                        it.uuid.toString(),
                                        it.characteristics
                                )
                            }?.toMutableList() ?: arrayListOf(), this@BLEDeviceActivity
                    )
                    binding.bleDeviceRecycler.layoutManager = LinearLayoutManager(this@BLEDeviceActivity)
                }
            }

            override fun onCharacteristicRead(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?, status: Int) {
                super.onCharacteristicRead(gatt, characteristic, status)
                runOnUiThread {
                    binding.bleDeviceRecycler.adapter?.notifyDataSetChanged()
                }
            }

        override fun onCharacteristicWrite(
                gatt: BluetoothGatt?,
                characteristic: BluetoothGattCharacteristic?,
                status: Int
        ) {
            super.onCharacteristicWrite(gatt, characteristic, status)
            runOnUiThread {
                binding.bleDeviceRecycler.adapter?.notifyDataSetChanged()
            }
        }
        override fun onCharacteristicChanged(
                gatt: BluetoothGatt,
                characteristic: BluetoothGattCharacteristic
        ) {
            super.onCharacteristicChanged(gatt, characteristic)
            runOnUiThread {
                binding.bleDeviceRecycler.adapter?.notifyDataSetChanged()
            }
        }


    }
    private fun connectionStateChange(newState: Int, gatt: BluetoothGatt?){
        BLEConnexionState.getBLEConnexionFromState(newState)?.let {
            runOnUiThread {
                binding.deviceStatus.text = getString(R.string.ble_device_status, getString(it.text))
            }
            if(it.state == BLEConnexionState.STATE_CONNECTED.state) {
                gatt?.discoverServices()
            }

        }
    }
    companion object {
        private const val STATE_DISCONNECTED = "DECONNECTE"
        private const val STATE_CONNECTED = "CONNEXION"
    }
}