package com.hemaapp.dtyw;

import com.hemaapp.hm_FrameWork.HemaNetTask;

import java.util.HashMap;

/**
 * Created by lenovo on 2016/9/6.
 */
public abstract  class DtywNetTask extends HemaNetTask{

    /**
     * ʵ��������������
     *
     * @param information
     *            ����������Ϣ
     * @param params
     *            �������?������,����ֵ)
     */
    public DtywNetTask(DtywHttpInformation information,
                       HashMap<String, String> params) {
        this(information, params, null);
    }

    /**
     * ʵ��������������
     *
     * @param information
     *            ����������Ϣ
     * @param params
     *            �������?������,����ֵ)
     * @param files
     *            �����ļ���(������,�ļ��ı���·��)
     */
    public DtywNetTask(DtywHttpInformation information,
                       HashMap<String, String> params, HashMap<String, String> files) {
        super(information, params, files);
    }

    @Override
    public DtywHttpInformation getHttpInformation() {
        return (DtywHttpInformation) super.getHttpInformation();
    }


}
