package com.yuyakaido.android.cardstackview.sample

import android.animation.Animator
import android.animation.AnimatorInflater
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.widget.RecyclerView
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class CityAdapter(
        private var cities: List<City> = emptyList()
) : RecyclerView.Adapter<CityAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.item_city, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val city = cities[position]
        holder.indicator.setupWithViewPager(holder.pager)
        holder.indicator.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) = Unit
            override fun onTabUnselected(tab: TabLayout.Tab?) = Unit
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    val spot = city.spots[it.position]
                    holder.spot.text = context.getString(R.string.spot_name, spot.id, spot.name)
                }
            }
        })
        holder.city.text = context.getString(R.string.spot_name, city.id, city.name)
        holder.pager.adapter = SpotAdapter(
                spots = city.spots,
                listener = object : SpotAdapter.Listener {
                    override fun onClick(area: TouchArea) {
                        val currentIndex = holder.pager.currentItem
                        when (area) {
                            TouchArea.Left -> {
                                if (currentIndex == 0) {
                                    val animator = AnimatorInflater.loadAnimator(context, R.animator.overshot_left)
                                    animator.setTarget(holder.itemView)
                                    animator.start()
                                    animator.addListener(object : Animator.AnimatorListener {
                                        override fun onAnimationRepeat(animation: Animator?) = Unit
                                        override fun onAnimationCancel(animation: Animator?) = Unit
                                        override fun onAnimationStart(animation: Animator?) = Unit
                                        override fun onAnimationEnd(animation: Animator?) {
                                            holder.itemView.performHapticFeedback(
                                                    HapticFeedbackConstants.LONG_PRESS
                                            )
                                            animator.removeAllListeners()
                                        }
                                    })
                                } else {
                                    holder.pager.setCurrentItem(currentIndex - 1, false)
                                }
                            }
                            TouchArea.Right -> {
                                if (currentIndex == city.spots.size - 1) {
                                    val animator = AnimatorInflater.loadAnimator(context, R.animator.overshot_right)
                                    animator.setTarget(holder.itemView)
                                    animator.start()
                                    animator.addListener(object : Animator.AnimatorListener {
                                        override fun onAnimationRepeat(animation: Animator?) = Unit
                                        override fun onAnimationCancel(animation: Animator?) = Unit
                                        override fun onAnimationStart(animation: Animator?) = Unit
                                        override fun onAnimationEnd(animation: Animator?) {
                                            holder.itemView.performHapticFeedback(
                                                    HapticFeedbackConstants.LONG_PRESS
                                            )
                                            animator.removeAllListeners()
                                        }
                                    })
                                } else {
                                    holder.pager.setCurrentItem(currentIndex + 1, false)
                                }
                            }
                        }
                    }
                })
    }

    override fun getItemCount(): Int {
        return cities.size
    }

    fun getCities(): List<City> {
        return cities
    }

    fun setCities(cities: List<City>) {
        this.cities = cities
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val indicator: TabLayout = view.findViewById(R.id.tab_layout)
        val spot: TextView = view.findViewById(R.id.item_spot)
        val city: TextView = view.findViewById(R.id.item_city)
        val pager: ViewPager = view.findViewById(R.id.view_pager)
    }

}