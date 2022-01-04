from django.contrib import admin
from mainapp.models import *
# Register your models here.

@admin.register(UserDeviceToken)
class CategoryAdmin(admin.ModelAdmin):
    list_display=('user',)

@admin.register(Category)
class CategoryAdmin(admin.ModelAdmin):
    list_display=('title',)


@admin.register(Review)
class ReviewAdmin(admin.ModelAdmin):
    list_display=('title','img', 'created_by', 'ISBN', 'publisher','content', 'rating', 'created_at')


@admin.register(Comment)
class CommentAdmin(admin.ModelAdmin):
    list_display=('content','created_at', 'created_by', 'review')

@admin.register(UserProfile)
class UserProfileAdmin(admin.ModelAdmin):
    list_display=('user', 'img')

