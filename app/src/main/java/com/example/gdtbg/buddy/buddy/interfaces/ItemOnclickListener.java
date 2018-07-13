package com.example.gdtbg.buddy.buddy.interfaces;

/**
 * 리사이클러뷰에서 항목을 클릭하였을 시, 클릭 리스너를 구현 하기 위해 상속 하는 메소드.
 * 이 클릭 리스너를 사용하면 뷰 홀더가 아닌 어뎁터 클래스에서 클릭 메소드의 구현이 가능함.
 */

public interface ItemOnclickListener {

    public void onListItemClick(int position);
    
}
