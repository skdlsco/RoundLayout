package com.eka.sample.roundlayout

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.eka.sample.roundlayout.databinding.ItemSeekBinding
import com.github.nitrico.lastadapter.LastAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.layoutManager = LinearLayoutManager(this)
        LastAdapter(SeekBarItem.values().toList(), BR.item)
            .map<SeekBarItem, ItemSeekBinding>(R.layout.item_seek)
            {
                onBind {
                    when (it.binding.item!!) {

                        SeekBarItem.RADIUS -> {
                            it.binding.seekBar.onProgressChange { _, p, _ ->
                                roundLayout.setRadius(p.toFloat(), p.toFloat(), p.toFloat(), p.toFloat())
                            }
                        }
                        SeekBarItem.TOP_LEFT_RADIUS -> {
                            it.binding.seekBar.onProgressChange { _, p, _ ->
                                roundLayout.mTopLeftRadius = p.toFloat()
                                roundLayout.requestLayout()
                                it.binding.value.text = p.toString()
                            }
                        }
                        SeekBarItem.TOP_RIGHT_RADIUS -> {
                            it.binding.seekBar.onProgressChange { _, p, _ ->
                                roundLayout.mTopRightRadius = p.toFloat()
                                roundLayout.requestLayout()
                                it.binding.value.text = p.toString()
                            }
                        }
                        SeekBarItem.BOTTOM_LEFT_RADIUS -> {
                            it.binding.seekBar.onProgressChange { _, p, _ ->
                                roundLayout.mBottomLeftRadius = p.toFloat()
                                roundLayout.requestLayout()
                                it.binding.value.text = p.toString()
                            }
                        }
                        SeekBarItem.BOTTOM_RIGHT_RADIUS -> {
                            it.binding.seekBar.onProgressChange { _, p, _ ->
                                roundLayout.mBottomRightRadius = p.toFloat()
                                roundLayout.requestLayout()
                                it.binding.value.text = p.toString()
                            }
                        }
                        SeekBarItem.STROKE_WIDTH -> {
                            it.binding.seekBar.onProgressChange { _, p, _ ->
                                roundLayout.strokeWidth = p.toFloat()
                                roundLayout.requestLayout()
                                it.binding.value.text = p.toString()
                            }
                        }
                    }
                }
            }
            .into(recyclerView)

    }
}
