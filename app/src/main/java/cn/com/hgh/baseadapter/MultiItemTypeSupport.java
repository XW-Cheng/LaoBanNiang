package cn.com.hgh.baseadapter;

public interface MultiItemTypeSupport<T>
{
	int getLayoutId(int position , T t);
	
	int getViewTypeCount();
	
	int getItemViewType(int postion,T t );
}