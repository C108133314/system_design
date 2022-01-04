from django.contrib.auth.models import User, Group
from django.db import models
from rest_framework import serializers
from mainapp.models import Review

class UserSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = User
        fields = ['url', 'username', 'email', 'groups']


class ReviewSerializer(serializers.ModelSerializer):
    class Meta:
        model = Review
        fields = ['title','img', 'created_by', 'ISBN', 'publisher','content', 'rating', 'created_at', 'created_by__email']

    def get_queryset(self):
        queryset = Review.objects.all()
        review_type = self.request.query_params.get("review_type")
        if review_type != None:
            if review_type == "latest_review":
                queryset = queryset.order_by('-created_at')
            elif review_type == "hot_review":
                queryset = queryset.order_by('-counter')
        return queryset