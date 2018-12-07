package com.moonpig.mvisample.productdetail

import com.moonpig.mvisample.domain.ProductDetailAction
import com.moonpig.mvisample.domain.ProductDetailResult
import com.moonpig.mvisample.domain.ProductDetailUseCase
import com.moonpig.mvisample.mvibase.BaseIntent
import com.moonpig.mvisample.mvibase.BaseViewModel
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

class ProductDetailViewModel(productDetailUseCase: ProductDetailUseCase,
                             productDetailTracker: ProductDetailTracker) :
        BaseViewModel<ProductDetailIntent, ProductDetailAction, ProductDetailResult, ProductDetailScreenViewState>(productDetailUseCase,
                                                                                                                   productDetailTracker) {

    override fun intentFilter(): ObservableTransformer<ProductDetailIntent, ProductDetailIntent> =
            ObservableTransformer { observable ->
                observable.publish {
                    Observable.merge(it.ofType(ProductDetailIntent.Initial::class.java).take(1),
                                     it.filter { intent -> intent !is ProductDetailIntent.Initial })
                }
            }

    override fun initialViewState(): ProductDetailScreenViewState = ProductDetailScreenViewState()

    override fun actionFrom(intent: ProductDetailIntent): ProductDetailAction =
            when (intent) {
                is ProductDetailIntent.Initial -> ProductDetailAction.LoadProductDetail
                is ProductDetailIntent.AddToBasket -> ProductDetailAction.AddProductToBasket(intent.productId, intent.quantity)
            }

    override fun reduce(previousViewState: ProductDetailScreenViewState, result: ProductDetailResult): ProductDetailScreenViewState =
            when (result) {
                is ProductDetailResult.GetProductDetail.InFlight -> previousViewState.copy(getProductDetailInFlight = true)
                is ProductDetailResult.GetProductDetail.Success -> previousViewState.copy(getProductDetailInFlight = false,
                                                                                          productDetail = ProductDetailViewState(name = result.name,
                                                                                                                                 description = result.description,
                                                                                                                                 price = result.price,
                                                                                                                                 imageUrl = result.imageUrl))
                is ProductDetailResult.GetProductDetail.Error -> previousViewState.copy(getProductDetailInFlight = false,
                                                                                        getProductDetailError = result.throwable)

                is ProductDetailResult.AddProduct.InFlight -> previousViewState.copy(addToBasketInFlight = true)
                is ProductDetailResult.AddProduct.Success -> previousViewState.copy(addToBasketInFlight = false)
                is ProductDetailResult.AddProduct.Error -> previousViewState.copy(addToBasketInFlight = false,
                                                                                  addToBasketError = result.throwable)
            }
}

sealed class ProductDetailIntent : BaseIntent {
    object Initial : ProductDetailIntent()
    data class AddToBasket(val productId: String, val quantity: Int) : ProductDetailIntent()
}

