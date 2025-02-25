package com.gustavo.bankandroid.features.statements.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gustavo.bankandroid.common.base.BaseViewModel
import com.gustavo.bankandroid.domain.contracts.StatementUseCases
import com.gustavo.bankandroid.entity.UserInfo
import com.gustavo.bankandroid.entity.UserStatementItem
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class StatementViewModel(
    private val fetchStatementUseCase: StatementUseCases.FetchStatementUseCase,
    private val getLoggedUserInfoUseCase: StatementUseCases.GetLoggedUserInfoUseCase,
    private val clearUserInfoUseCase: StatementUseCases.ClearUserInfoUseCase
) : BaseViewModel() {

    override val compositeDisposable = CompositeDisposable()

    private val _showErrorLiveData = MutableLiveData<Boolean>()
    val showErrorLiveData:LiveData<Boolean>
        get() = _showErrorLiveData

    private val _statementListLiveData = MutableLiveData<List<UserStatementItem>>()
    val statementListLiveData: LiveData<List<UserStatementItem>>
        get() = _statementListLiveData

    private val _userInfoLiveData = MutableLiveData<UserInfo>()
    val userInfo: LiveData<UserInfo>
        get() = _userInfoLiveData

    private val _userLoggedOutLiveData = MutableLiveData<Boolean>()
    val userLoggedOutLiveData: LiveData<Boolean>
        get() = _userLoggedOutLiveData

    private val _isLoadingLiveData = MutableLiveData<Boolean>()
    val isLoadingLiveData: LiveData<Boolean>
        get() = _isLoadingLiveData

    fun fetchData(){
        val disposable = getLoggedUserInfoUseCase.execute()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _userInfoLiveData.value = it
                fetchStatements(it)
            },{
                showError()
            })
        compositeDisposable.add(disposable)
    }

    fun logout(){
        val disposable = Completable.fromAction { clearUserInfoUseCase.execute() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({ _userLoggedOutLiveData.value = true },{})
        compositeDisposable.add(disposable)
    }

    private fun fetchStatements(userInfo:UserInfo) {
        _isLoadingLiveData.value = true
        val disposable = fetchStatementUseCase.execute(userInfo.userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _statementListLiveData.postValue(it)
                _isLoadingLiveData.value = false
            },{
                showError()
                _isLoadingLiveData.value = false
            })
        compositeDisposable.add(disposable)
    }

    private fun showError() {
        _showErrorLiveData.value = true
    }


}