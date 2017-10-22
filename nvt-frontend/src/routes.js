import Vue from 'vue'
import VueRouter from 'vue-router'
import Combinations from './components/combination/combinations'
import NewCombination from './components/combination/create-combination.vue'
import EditCombination from './components/combination/edit-combination.vue'
import StaticAnimation from './components/combination/animations/static-animation.vue'
import DynamicAnimation from './components/combination/animations/dynamic-animation.vue'

Vue.use(VueRouter)

let mainPageResolver = (to, from, next) => {
    next('/combinations')
}


const router = new VueRouter({
    mode: 'history',
    linkActiveClass: 'active',
    routes: [
        {
            path: '/',
            component: Combinations,
            beforeEnter: (to, from, next) => {
                mainPageResolver(to, from, next)
            }
        },
        {
            path: '/combinations',
            component: Combinations,
            name: 'combinations'
        },
        {
            path: '/combinations/new',
            component: NewCombination,
            name: 'new-combination'
        },
        {
            path: '/combinations/edit',
            component: EditCombination,
            name: 'edit-combination'
        },
        {
            path: '/combinations/static',
            component: StaticAnimation,
            name: 'static-animation'
        },
        {
            path: '/combinations/dynamic',
            component: DynamicAnimation,
            name: 'dynamic-animation'
        },
        {
            path: '*',
            redirect: '/'
        }
    ]
})

export default router