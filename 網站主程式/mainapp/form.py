from django import forms
from django.contrib import auth
from django.contrib.auth.models import User
from .models import *

class RegForm(forms.Form):
    name = forms.CharField(label='姓名',widget=forms.TextInput(attrs={'class':'form-control','placeholder':'請輸入真實姓名'})) #註冊姓名更改
    nickname = forms.CharField(label='暱稱',widget=forms.TextInput(attrs={'class':'form-control','placeholder':'請輸入暱稱'}))
    email= forms.CharField(label='帳號(學校信箱)',
                                max_length=30,
                                min_length=3,
                                widget=forms.EmailInput(
                                    attrs={'class':'form-control','placeholder':'請輸入高科大電子信箱作為用帳號'}
                                ))
    password = forms.CharField(label='密碼',
                                max_length=16,
                                min_length=8,
                                widget=forms.PasswordInput(
                                    attrs={'class':'form-control','placeholder':'請輸入8~16位密碼'}
                                ))
    password_again= forms.CharField(label='再輸入一次密碼',
                                max_length=16,
                                min_length=8,
                                widget=forms.PasswordInput(
                                    attrs={'class':'form-control','placeholder':'請再輸入一次密碼'}
                                ))

    def __init__(self, *args, **kwargs):
        if 'request' in kwargs:
            self.request = kwargs.pop('request')
        super(RegForm, self).__init__(*args, **kwargs)
        
    def clean_username(self):
        email=self.cleaned_data['email']
        if User.objects.filter(email=email).exists():
            raise forms.ValidationError('用戶名已存在')
        return email
    def clean_password_again(self):
        password=self.cleaned_data['password']
        password_again=self.cleaned_data['password_again']
        if password !=password_again:
            raise forms.ValidationError('輸入的密碼不一致')  
        return password_again


