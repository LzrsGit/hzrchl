package com.znkj.rchl_hz.pullableview;

public interface Pullable
{
	/**
	 * �ж��Ƿ�����������������Ҫ�������ܿ���ֱ��return false
	 * 
	 * @return true��������������򷵻�false
	 */
	boolean canPullDown();

	/**
	 * �ж��Ƿ�����������������Ҫ�������ܿ���ֱ��return false
	 * 
	 * @return true��������������򷵻�false
	 */
	boolean canPullUp();
}