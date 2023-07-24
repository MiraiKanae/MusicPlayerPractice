using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.EventSystems;
using UnityEngine.UI;

public class Change_Play_Stop_pp : MonoBehaviour
{
    private AndroidJavaObject mp_a;
    private AndroidJavaObject mp_s;

    private void Start()
    {
        using (mp_a = new AndroidJavaObject("com.example.MP_MainActivity"))
        {
            mp_a.Call("onCreate");
            Debug.Log("setContext");
        }
    }
}
