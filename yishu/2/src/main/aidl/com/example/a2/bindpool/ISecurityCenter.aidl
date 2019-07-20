// ISecurityCenter.aidl
package com.example.a2.bindpool;

//用来进行加密的
interface ISecurityCenter {
    String encrypt(String content);
    String decrypt(String password);
}
