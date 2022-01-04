
from django.urls import path, include
from django.conf import settings
from . import views

urlpatterns = [
    path('get-review/', views.get_review),
    path('add-review/', views.addReview),
    path('update-review/', views.updateReview),
    path('delete-review/', views.deleteReview),
    path("get-review-detail/", views.get_review_detail),
    path('get-category/', views.get_category),
    path('get-comment/', views.get_comment),
    path('add-comment/', views.addComment),
    path("update-comment/", views.updateComment),
    path("delete-comment/", views.deleteComment),
    path("login/", views.log_in),
    path("upload_image/", views.upload_image),
]