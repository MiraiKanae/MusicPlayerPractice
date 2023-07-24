using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.EventSystems;
using UnityEngine.UI;

public class Change_Play_Stop_pp : MonoBehaviour
    //, IPointerClickHandler, IPointerDownHandler, IPointerUpHandler
{
    public System.Action onClickCallback;

    [SerializeField] GameObject change_object;
    [SerializeField] Image change_image;
    [SerializeField] Sprite play_image;
    [SerializeField] Sprite stop_image;

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

    /*
    public void OnPointerClick(PointerEventData eventData)
    {
        onClickCallback?.Invoke();

        if(change_image.sprite == play_image)
        {
            change_image.sprite = stop_image;
            using (mp_s = new AndroidJavaObject("com.example.MP_MusicService"))
            {
                Debug.Log("reqPause_True");
                mp_s.Call("onPause");
            }
        }
        else if(change_image.sprite == stop_image)
        {
            change_image.sprite = play_image;
            using (mp_s = new AndroidJavaObject("com.example.MP_MusicService"))
            {
                Debug.Log("reqStart_True");
                mp_s.Call("onStart");
            }
        }
    }
    //機能：ボタンが押される
    public void OnPointerDown(PointerEventData eventData)
    {
        //change_object.transform.localScale = new Vector2(0.8f,0.8f);
        change_image.color = new Color32(200, 200, 200, 255);
    }
    // ボタンが離される
    public void OnPointerUp(PointerEventData eventData)
    {
        //change_object.transform.localScale = new Vector2(1.0f, 1.0f);
        change_image.color = new Color32(255, 255, 255, 255);
    }
    */
}
