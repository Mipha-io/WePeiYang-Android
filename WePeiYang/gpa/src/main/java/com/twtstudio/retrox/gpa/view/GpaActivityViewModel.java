package com.twtstudio.retrox.gpa.view;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.widget.Toast;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.tencent.bugly.crashreport.CrashReport;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.twtstudio.retrox.gpa.BR;
import com.twtstudio.retrox.gpa.GpaBean;
import com.twtstudio.retrox.gpa.GpaProvider;
import com.twtstudio.retrox.gpa.R;

import me.tatarka.bindingcollectionadapter.ItemViewSelector;
import me.tatarka.bindingcollectionadapter.collections.MergeObservableList;
import me.tatarka.bindingcollectionadapter.itemviews.ItemViewClassSelector;

/**
 * Created by retrox on 2017/1/28.
 */

public class GpaActivityViewModel implements ViewModel {

    private RxAppCompatActivity mRxActivity;

    public final ObservableField<GpaBean> obGpaBean = new ObservableField<>();

    /**
     * 非header的item结合
     */
    public final ObservableArrayList<ViewModel> mViewModels = new ObservableArrayList<>();

    public final GpaChartViewModel headerViewModel = new GpaChartViewModel();

    public final MergeObservableList<ViewModel> mMergeObservableList = new MergeObservableList<>();

    public GpaActivityViewModel(RxAppCompatActivity rxActivity) {
        mRxActivity = rxActivity;
    }

    public final ReplyCommand<Integer> valueSelectCommand = new ReplyCommand<>(this::setTermIndex);

    public final ItemViewSelector itemView = ItemViewClassSelector.builder()
            .put(GpaChartViewModel.class, BR.viewModel, R.layout.gpa_item_chart)
            .put(TermBriefViewModel.class,BR.viewModel,R.layout.gpa_item_term_brief)
            .put(TermDetailViewModel.class,BR.viewModel,R.layout.gpa_item_term)
            .build();

    public void getGpaData(boolean update){
        /**
         * for animation use
         * shared elements
         */


        GpaProvider.init(mRxActivity)
                .registerAction(gpaBean -> {

                    if (mMergeObservableList.size()!=0){
                        mMergeObservableList.removeItem(headerViewModel);
                        mMergeObservableList.removeList(mViewModels);
                    }

                    mMergeObservableList.insertItem(headerViewModel);


                    headerViewModel.observableGpaBean.set(gpaBean);
                    headerViewModel.setProxy(valueSelectCommand);
                    obGpaBean.set(gpaBean);
//                    mMergeObservableList.insertItem(headerViewModel);
                    mMergeObservableList.insertList(mViewModels);
                    // TODO: 07/04/2017 也许明年大一的处理？ 不知道为什么后台返回空数据
                    try {
                        setTermIndex(gpaBean.data.size()-1);
                    }catch (IndexOutOfBoundsException e){
                        CrashReport.postCatchedException(e);
                    }
                })
                .getData(update);
    }

    public void getGpaData(){
        getGpaData(false);
    }

    public void setTermIndex(int index){
        GpaBean gpaBean = obGpaBean.get();
        mViewModels.clear();
        mViewModels.add(new TermBriefViewModel(gpaBean.data.get(index)));
        mViewModels.add(new TermDetailViewModel(gpaBean.data.get(index)));
    }

}
