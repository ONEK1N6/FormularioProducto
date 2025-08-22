package com.example.producto

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var etProductName: EditText
    private lateinit var etProductPrice: EditText
    private lateinit var spinnerCategory: Spinner
    private lateinit var checkBoxAvailability: CheckBox
    private lateinit var btnSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        setupCategorySpinner()
        setupSaveButton()
    }

    private fun initViews() {
        etProductName = findViewById(R.id.etProductName)
        etProductPrice = findViewById(R.id.etProductPrice)
        spinnerCategory = findViewById(R.id.spinnerCategory)
        checkBoxAvailability = findViewById(R.id.checkBoxAvailability)
        btnSave = findViewById(R.id.btnSave)
    }

    private fun setupCategorySpinner() {
        val categories = arrayOf(
            "Selecciona una categoría",
            "Electrónicos",
            "Ropa y Accesorios",
            "Hogar y Jardín",
            "Deportes",
            "Libros",
            "Automóviles",
            "Salud y Belleza",
            "Alimentos y Bebidas"
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = adapter
    }

    private fun setupSaveButton() {
        btnSave.setOnClickListener {
            if (validateForm()) {
                saveProduct()
            }
        }
    }

    private fun validateForm(): Boolean {
        val productName = etProductName.text.toString().trim()
        val productPrice = etProductPrice.text.toString().trim()
        val selectedCategory = spinnerCategory.selectedItemPosition

        // Validar nombre del producto
        if (productName.isEmpty()) {
            etProductName.error = "El nombre del producto es obligatorio"
            etProductName.requestFocus()
            return false
        }

        if (productName.length < 3) {
            etProductName.error = "El nombre debe tener al menos 3 caracteres"
            etProductName.requestFocus()
            return false
        }

        // Validar precio
        if (productPrice.isEmpty()) {
            etProductPrice.error = "El precio es obligatorio"
            etProductPrice.requestFocus()
            return false
        }

        try {
            val price = productPrice.toDouble()
            if (price <= 0) {
                etProductPrice.error = "El precio debe ser mayor a 0"
                etProductPrice.requestFocus()
                return false
            }
        } catch (e: NumberFormatException) {
            etProductPrice.error = "Ingresa un precio válido"
            etProductPrice.requestFocus()
            return false
        }

        // Validar categoría
        if (selectedCategory == 0) {
            Toast.makeText(this, "Selecciona una categoría", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun saveProduct() {
        val productName = etProductName.text.toString().trim()
        val productPrice = etProductPrice.text.toString().toDouble()
        val selectedCategory = spinnerCategory.selectedItem.toString()
        val isAvailable = checkBoxAvailability.isChecked

        // Crear objeto producto
        val product = Product(
            name = productName,
            price = productPrice,
            category = selectedCategory,
            isAvailable = isAvailable
        )

        // Aquí puedes guardar en base de datos, SharedPreferences, etc.
        Toast.makeText(
            this,
            "Producto guardado:\n" +
                    "Nombre: ${product.name}\n" +
                    "Precio: $${product.price}\n" +
                    "Categoría: ${product.category}\n" +
                    "Disponible: ${if (product.isAvailable) "Sí" else "No"}",
            Toast.LENGTH_LONG
        ).show()

        // Limpiar formulario después de guardar
        clearForm()
    }

    private fun clearForm() {
        etProductName.text.clear()
        etProductPrice.text.clear()
        spinnerCategory.setSelection(0)
        checkBoxAvailability.isChecked = false
        etProductName.requestFocus()
    }
}

// Clase de datos para el producto
data class Product(
    val name: String,
    val price: Double,
    val category: String,
    val isAvailable: Boolean
)