package com.moonpig.mvisample.domain

import com.moonpig.mvisample.domain.entities.ProductDetailEntity
import com.moonpig.mvisample.domain.mvibase.BaseAction
import com.moonpig.mvisample.domain.mvibase.BaseResult
import com.moonpig.mvisample.domain.mvibase.BaseUseCase
import io.reactivex.Observable

class ProductDetailUseCase(private val productDetailRepository: ProductDetailRepository) :
        BaseUseCase<ProductDetailAction, ProductDetailResult> {

    override fun resultFrom(action: ProductDetailAction): Observable<ProductDetailResult> {
        return when (action) {
            is ProductDetailAction.LoadProductDetail -> getProductDetail()
            is ProductDetailAction.AddProductToBasket -> addProductToBasket(action.productId, action.quantity)
        }
    }

    private fun getProductDetail(): Observable<ProductDetailResult> =
            productDetailRepository.getProductDetails()
                    .map {
                        when (it) {
                            is RepositoryState.GetProductDetail.InFlight -> ProductDetailResult.GetProductDetail.InFlight
                            is RepositoryState.GetProductDetail.Success -> ProductDetailResult.GetProductDetail.Success(name = it.productDetailEntity.name,
                                                                                                                        description = it.productDetailEntity.description,
                                                                                                                        price = it.productDetailEntity.price,
                                                                                                                        imageUrl = it.productDetailEntity.imageUrl)
                            is RepositoryState.GetProductDetail.Error -> ProductDetailResult.GetProductDetail.Error(it.throwable)
                        }
                    }

    private fun addProductToBasket(productId: String, quantity: Int): Observable<ProductDetailResult> =
            productDetailRepository.addProductToBasket(AddProductRequest(productId, quantity))
                    .map {
                        when (it) {
                            is RepositoryState.AddProduct.InFlight -> ProductDetailResult.AddProduct.InFlight
                            is RepositoryState.AddProduct.Success -> ProductDetailResult.AddProduct.Success
                            is RepositoryState.AddProduct.Error -> ProductDetailResult.AddProduct.Error(it.throwable)
                        }
                    }
}

interface ProductDetailRepository {
    fun getProductDetails(): Observable<RepositoryState.GetProductDetail>
    fun addProductToBasket(addProductRequest: AddProductRequest): Observable<RepositoryState.AddProduct>
}

sealed class RepositoryState {
    sealed class GetProductDetail : RepositoryState() {
        object InFlight : GetProductDetail()
        data class Success(val productDetailEntity: ProductDetailEntity) : GetProductDetail()
        data class Error(val throwable: Throwable) : GetProductDetail()
    }

    sealed class AddProduct : RepositoryState() {
        object InFlight : AddProduct()
        object Success : AddProduct()
        data class Error(val throwable: Throwable) : AddProduct()
    }
}

sealed class ProductDetailResult : BaseResult {
    sealed class GetProductDetail : ProductDetailResult() {
        object InFlight : GetProductDetail()
        data class Success(val name: String, val description: String, val price: Int, val imageUrl: String) :
                GetProductDetail()

        data class Error(val throwable: Throwable) : GetProductDetail()
    }

    sealed class AddProduct : ProductDetailResult() {
        object InFlight : AddProduct()
        object Success : AddProduct()
        data class Error(val throwable: Throwable) : AddProduct()
    }
}

sealed class ProductDetailAction : BaseAction {
    object LoadProductDetail : ProductDetailAction()
    data class AddProductToBasket(val productId: String, val quantity: Int) : ProductDetailAction()
}

data class AddProductRequest(val productId: String, val quantity: Int)