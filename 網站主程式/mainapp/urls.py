from django.contrib import admin
from django.urls import path, include
from .views import *

app_name = "mainapp" 

urlpatterns = [
    path('', index),
    path('add-Category/', addCategory),
    path('add-Review/', addReview),
    path('deleteReview/<int:pk>', deleteReview),
    path('updateReview/<int:pk>', updateReview),
    path('deleteComment/<int:Review_pk>/<int:pk>', deleteComment),
    path('updateComment/<int:Review_pk>/<int:pk>', updateComment),
    path('review-List/', reviewList),
    path('reviewDetail/<int:pk>', reviewDetail),
    path('addComment/<int:pk>', addComment),
    path('log-in/',log_in),
    path('log-out/',log_out),
    path('register/',register),
]
