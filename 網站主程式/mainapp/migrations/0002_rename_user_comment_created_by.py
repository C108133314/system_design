# Generated by Django 3.2.3 on 2021-06-08 11:01

from django.db import migrations


class Migration(migrations.Migration):

    dependencies = [
        ('mainapp', '0001_initial'),
    ]

    operations = [
        migrations.RenameField(
            model_name='comment',
            old_name='user',
            new_name='created_by',
        ),
    ]
